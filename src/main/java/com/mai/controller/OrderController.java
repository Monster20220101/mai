package com.mai.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.mai.common.dto.SettleDto;
import com.mai.common.lang.Result;
import com.mai.entity.Order;
import com.mai.entity.Product;
import com.mai.entity.User;
import com.mai.service.OrderService;
import com.mai.service.ProductService;
import com.mai.shiro.UserProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    /**
     * 【用户】批量生成订单
     */
    @RequiresAuthentication
    @PostMapping("/order")
    public Result order(@RequestBody List<SettleDto> settleDtoList) {
        ArrayList<Order> orders = new ArrayList<>();
        // 对商品进行销量增加
        for (SettleDto settleDto : settleDtoList) {
            Order order = new Order();
            BeanUtil.copyProperties(settleDto, order, "orderId"
                    , "orderStatus", "createTime", "updateTime");
            orders.add(order);
            Product product = productService.getById(settleDto.getProductId());
            product.setProductSales(product.getProductSales() + settleDto.getNum());
            productService.updateById(product);
        }
        // 保存订单
        orderService.saveBatch(orders);
        return Result.succ(null);
    }

    /**
     * 【用户】批量查看订单
     */
    @RequiresAuthentication
    @GetMapping("/userOrder")
    public Result userOrder() {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        List<Order> orders = orderService.list(new QueryWrapper<Order>()
                .eq("user_id", userProfile.getUserId()).orderByDesc("create_time"));
        return Result.succ(orders);
    }

    /**
     * 【用户】收货
     */
    @RequiresAuthentication
    @PutMapping("/receive/{orderId}")
    public Result receive(@PathVariable Long orderId) {
        orderService.update(new Order().setOrderId(orderId).setOrderStatus(2),
                new QueryWrapper<Order>().eq("order_id", orderId));
        return Result.succ(null);
    }

    /**
     * 【商家】查看自家订单
     */
    @RequiresAuthentication
    @GetMapping("/managerOrder")
    public Result managerOrder() {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        List<Order> orders = orderService.list(new QueryWrapper<Order>()
                .eq("store_name", userProfile.getUserNickname()));
        return Result.succ(orders);
    }

    /**
     * 【商家】发货
     */
    @RequiresRoles("manager")
    @RequiresAuthentication
    @PutMapping("/send/{orderId}")
    public Result send(@PathVariable Long orderId) {
        orderService.update(new Order().setOrderId(orderId).setOrderStatus(1),
                new QueryWrapper<Order>().eq("order_id", orderId));
        return Result.succ(null);
    }
}
