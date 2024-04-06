package com.mai.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.mai.common.dto.LoginDto;
import com.mai.common.lang.Result;
import com.mai.entity.Imgs;
import com.mai.entity.Product;
import com.mai.entity.User;
import com.mai.service.ImgsService;
import com.mai.service.ProductService;
import com.mai.service.UserService;
import com.mai.shiro.UserProfile;
import com.mai.util.JwtUtils;
import com.mai.util.MyFileUtil;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @RestController：用于定义 RESTful 接口，
 * 相当于 @Controller 和 @ResponseBody 的组合。
 *
 * @RequestMapping：用于定义 HTTP 请求的映射，用于处理 HTTP 请求。
 *
 * @Autowired：自动注入注解，用于自动装配 Spring Bean。
 *
 * @Value：属性注入注解，用于注入配置文件中的属性值。
 */
@RestController
public class UserController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    ImgsService imgsService;
    @Autowired
    ProductService productService;

    /**
     * 登录
     */
    // @CrossOrigin // 配置跨域。由于已经全局配置了所以不需要(但是别删这行注解)
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // @RequestBody 指定返回json数据
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", loginDto.getUserId()));

        // 当不满足断言条件时，会抛出IllegalArgumentException或IllegalStateException异常
        Assert.notNull(user, "用户不存在");
        if (user.getUserFlag() == 1) {
            return Result.fail("账号被封禁！");
        }

        if (!user.getUserPassword().equals(SecureUtil.md5(loginDto.getUserPassword()))) {
            // 不交给shiro Realm判断。这是整合JWT，不是原生token，JWt中是没有前端传入的密码的
            return Result.fail("密码错误！");
        }
        String jwt;
        if(loginDto.getIsAutoLogin()==false) {
            jwt = jwtUtils.generateToken(user.getUserId(), 10);//10s过期
        } else {
            jwt = jwtUtils.generateToken(user.getUserId());
        }
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 用户可以另一个接口
        user.setUserPassword("");
        return Result.succ(user);
    }

    /**
     * 退出
     */
    @GetMapping("/logout")
    //@RequiresAuthentication // 这个注解表示需要登录才能访问/logout接口
    // 相关注解见 http://t.zoukankan.com/pureEve-p-6727982.html
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    /**
     * 注册-时序图
     */
    @PostMapping("/register")
    public Result register(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = new User();
        user.setUserId(loginDto.getUserId());
        user.setUserEmail(loginDto.getUserEmail());
        String userPassword = SecureUtil.md5(loginDto.getUserPassword());
        user.setUserPassword(userPassword);

        try {
            userService.save(user);
        } catch (Exception e) {
            return Result.fail("手机号重复！");
        }

        user = userService.getOne(new QueryWrapper<User>().eq("user_id", loginDto.getUserId()));
        String jwt = jwtUtils.generateToken(user.getUserId());

        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        System.out.println(Result.succ(MapUtil.builder()
                .put("userEmail", user.getUserEmail())
                .put("userId", user.getUserId())
                .map()
        ));
        user.setUserPassword("");
        return Result.succ(user);
    }

    /**
     * 【用户】修改个人信息
     */
    @RequiresAuthentication
    @PutMapping("/personal")
    public Result personal(@RequestParam(value = "files", required = false) MultipartFile[] files,
                           @Validated User user) throws IOException {
        if (files != null && files.length != 0) {
            Long imgsId = MyFileUtil.uploadFiles(files, "user");
            Imgs imgs = imgsService.getOne(new QueryWrapper<Imgs>()
                    .eq("imgs_id", imgsId).last("limit 1"));
            user.setUserHead(imgs.getImgUrl());
        }
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        String userId = userProfile.getUserId();
        userService.update(user, new UpdateWrapper<User>()
                .eq("user_id", userId));
        user = userService.getOne(new QueryWrapper<User>().eq("user_id", userId));
        // 将关联的商品修改
        productService.update(new Product().setStoreName(user.getUserNickname())
                , new QueryWrapper<Product>().eq("store_name", userProfile.getUserNickname()));
        user.setUserPassword("");
        return Result.succ(user);
    }

    /**
     * 【用户】申请成为商家
     */
    @RequiresAuthentication
    @PutMapping("/apply")
    public Result apply(@RequestBody User user) {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        String userId = userProfile.getUserId();
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", userId);
        userService.update(user.setUserId(userId).setUserStatus(1), wrapper);
        user = userService.getOne(wrapper);
        user.setUserPassword("");
        return Result.succ(user);
    }

    /**
     * 【管理员】获取用户列表
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @GetMapping("/user")
    public Result user() {
        List<User> list = userService.list(new QueryWrapper<User>()
                .ne("user_status", 3));
        return Result.succ(list);
    }

    /**
     * 【管理员】封禁用户(用户被封禁后无法登录)
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @DeleteMapping("/user/{userId}")
    public Result delete(@PathVariable String userId) {
        userService.update(new User().setUserFlag(1)
                , new QueryWrapper<User>().eq("user_id", userId));
        return Result.succ(null);
    }

    /**
     * 【管理员】解禁用户
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @PutMapping("/unblock/{userId}")
    public Result unblock(@PathVariable String userId) {
        userService.update(new User().setUserFlag(0)
                , new QueryWrapper<User>().eq("user_id", userId));
        return Result.succ(null);
    }

    /**
     * 【管理员】查看申请成为商家的用户列表
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @GetMapping("/userApply")
    public Result userApply() {
        List<User> list = userService.list(new QueryWrapper<User>()
                .eq("user_status", 1));
        return Result.succ(list);
    }

    /**
     * 【管理员】同意用户的申请
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @PutMapping("/agree/{userId}")
    public Result agree(@PathVariable String userId) {
        userService.update(new User().setUserStatus(2)
                , new QueryWrapper<User>().eq("user_id", userId));
        return Result.succ(null);
    }

    /**
     * 【管理员】拒绝用户的申请
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @PutMapping("/disagree/{userId}")
    public Result disagree(@PathVariable String userId) {
        userService.update(new User().setUserStatus(0)
                , new QueryWrapper<User>().eq("user_id", userId));
        return Result.succ(null);
    }
}
