package com.mai.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.mai.common.lang.Result;
import com.mai.entity.*;
import com.mai.service.CmtService;
import com.mai.service.ImgsService;
import com.mai.service.OrderService;
import com.mai.shiro.UserProfile;
import com.mai.util.MyFileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
public class CmtController {
    @Autowired
    CmtService cmtService;
    @Autowired
    OrderService orderService;
    @Autowired
    ImgsService imgsService;

    /**
     * 【用户】添加评论-时序图
     */
    @RequiresAuthentication
    @PostMapping("/cmt")
    public Result add(@RequestParam(value = "files",
            required = false) MultipartFile[] files,
                      @Validated Cmt cmt, Long orderId) throws IOException {

        Order order = orderService.getById(orderId);
        if (order.getOrderStatus() != 2) {
            return Result.fail("暂无评论权限！");
        }

        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        cmt.setUserNickname(userProfile.getUserNickname());
        cmt.setUserHead(userProfile.getUserHead());

        if (files != null && files.length != 0) {
            Long imgsId = MyFileUtil.uploadFiles(files, "cmt");
            cmt.setImgsId(imgsId);
        }

        cmtService.save(cmt);
        orderService.update(order.setOrderStatus(3), new QueryWrapper<Order>()
                .eq("order_id", orderId));
        return Result.succ(null);
    }

    /**
     * 【游客】查看商品评论
     * @return <code> List<Cmt>, Map<Long,Imgs> <code/>
     */
    @GetMapping("/cmt/{productId}")
    public Result userCmt(@PathVariable(name = "productId") Long productId) {
        List<Cmt> cmts = cmtService.list(new QueryWrapper<Cmt>().eq("product_id", productId));
        HashMap<Long, List<Imgs>> imgsCmtMap = new HashMap<>();
        for(Cmt cmt : cmts) {
            Long imgsId = cmt.getImgsId();
            if(imgsId != null) {
                List<Imgs> imgsCmt = imgsService.list(new QueryWrapper<Imgs>()
                        .eq("imgs_id", imgsId));
                imgsCmtMap.put(imgsId, imgsCmt);
            }
        }
        HashMap data = new HashMap<String, Object>();
        data.put("cmts", cmts);
        data.put("imgsCmtMap", imgsCmtMap);
        return Result.succ(data);
    }

    /**
     * 【管理员】查看评论
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @GetMapping("/adminCmt")
    public Result adminCmt() {
        List<Cmt> cmts = cmtService.list();
        return Result.succ(cmts);
    }

    /**
     * 【管理员】删除评论
     */
    @RequiresRoles("admin")
    @RequiresAuthentication
    @DeleteMapping("/cmt/{cmtId}")
    public Result delete(@PathVariable Long cmtId) {
        cmtService.remove(new QueryWrapper<Cmt>().eq("cmt_id", cmtId));
        return Result.succ(null);
    }
}
