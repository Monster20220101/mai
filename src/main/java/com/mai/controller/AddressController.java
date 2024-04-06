package com.mai.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mai.common.lang.Result;
import com.mai.entity.Address;
import com.mai.service.AddressService;
import com.mai.shiro.UserProfile;
import com.mai.util.MyUtil;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    /**
     * 【用户】查看地址
     */
    @RequiresAuthentication
    @GetMapping("/address")
    public Result address() {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        String userId = userProfile.getUserId();
        List<Address> addresses = addressService.list(new QueryWrapper<Address>().eq("user_id", userId));
        return Result.succ(addresses);
    }

    /**
     * 【用户】删除地址
     */
    @RequiresAuthentication
    @DeleteMapping("/address/{addressId}")
    public Result delete(@PathVariable(name = "addressId") Long addressId) {
        addressService.remove(new QueryWrapper<Address>().eq("address_id", addressId));
        return Result.succ(null);
    }

    /**
     * 【用户】设置默认地址
     */
    @RequiresAuthentication
    @PutMapping("/address/{addressId}")
    public Result setDefault(@PathVariable(name = "addressId") Long addressId) {
        // 将该用户的所有地址设成非默认
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        String userId = userProfile.getUserId();
        Address address = new Address();
        addressService.update(address.setAddressIsdefault(0),
                new QueryWrapper<Address>().eq("user_id", userId));
        // setDefault
        addressService.update(address.setAddressIsdefault(1),
                new QueryWrapper<Address>().eq("address_id", addressId));

        return Result.succ(null);
    }

    /**
     * 【用户】新增地址
     */
    @RequiresAuthentication
    @PostMapping("/address")
    public Result saveAddress(Address address, int province, int city, int area) {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        String userId = userProfile.getUserId();
        String addressRegion = MyUtil.getAddressRegion(province, city, area);
        address.setAddressRegion(addressRegion);
        address.setUserId(userId);

        // 默认地址处理
        if (address.getAddressIsdefault() != null && address.getAddressIsdefault() == 1) {
            addressService.update(new Address().setAddressIsdefault(0),
                    new QueryWrapper<Address>().eq("user_id", userId));
        }

        addressService.save(address);
        return Result.succ(null);
    }
}
