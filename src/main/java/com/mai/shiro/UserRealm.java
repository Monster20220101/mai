package com.mai.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mai.entity.User;
import com.mai.service.UserService;
import com.mai.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;


    /**
     * 为了让realm支持jwt的凭证校验
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权（登录之后是否具有某些权限）
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取当前登录的user
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();

        // 这个放 用户权限的CODE
        Set<String> permissionCode = new HashSet<>();
        // 这个放用户角色的名字
        Set<String> roleName = new HashSet<>();

        if (userProfile.getUserStatus() == 2) {
            roleName.add("manager");
        }
        if (userProfile.getUserStatus() == 3) {
            roleName.add("admin");
        }

        // 将 角色名称级权限的名称放进 info中 以供 shiroConfig 拦截
        info.setRoles(roleName);
        return info;
    }

    /**
     * 认证（登录） <br />
     * 通过jwt获取到用户信息，判断用户的状态，最后异常就抛出对应的异常信息。
     * 否则封装成SimpleAuthenticationInfo返回给shiro。
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwt = (JwtToken) token; // 强转superclass转subclass
        log.info("jwt----------------->{}", jwt);
        String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        //User user = userService.getById(Long.parseLong(userId));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if (user.getUserFlag() == 1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        UserProfile profile = new UserProfile();
        BeanUtil.copyProperties(user, profile);
        log.info("profile----------------->{}", profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
        // Credentials正确就通过，否则抛异常。这里jwt.getCredentials本来是user.getUserPassword
        // getName()获取当前的类名，也就是AccountRealm
    }
}