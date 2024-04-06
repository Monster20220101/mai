package com.mai.controller;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mai.common.lang.Result;
import com.mai.entity.*;
import com.mai.service.*;
import com.mai.shiro.UserProfile;
import com.mai.util.MyFileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ImgsService imgsService;
    @Autowired
    CmtService cmtService;
    @Autowired
    OrderService orderService;
    @Autowired
    AuthorService authorService;
    @Autowired
    ProductAuthorService productAuthorService;

    /**
     * 【游客】搜索
     */
    @GetMapping("/search/{keyword}")
    public Result search(@PathVariable(name = "keyword") String keyword) {
        QueryWrapper<Product> productWrapper = new QueryWrapper<Product>();
        productWrapper.eq("product_flag", 0);
        productWrapper.and(wrapper -> wrapper.like("product_name", keyword)
                .or().eq("product_type", keyword));
        List<Product> pList = productService.list(productWrapper);
        List<Imgs> iList = new ArrayList<>();
        for (Product p : pList) {
            if (p.getImgsId() != null) {
                QueryWrapper<Imgs> Imgswrapper = new QueryWrapper<Imgs>()
                        .eq("imgs_id", p.getImgsId()).last("limit 1");
                iList.add(imgsService.getOne(Imgswrapper));
            } else {
                iList.add(new Imgs());
            }
        }
        return Result.succ(MapUtil.builder()
                .put("pList", pList)
                .put("iList", iList)
                .map()
        );
    }


    /**
     * 【游客】主页图书排行
     */
    @GetMapping("/productRank")
    public Result productRank() {
        QueryWrapper<Product> productWrapper = new QueryWrapper<Product>();
        productWrapper.eq("product_flag", 0);
        productWrapper.orderByDesc("product_sales");
        productWrapper.last("limit 9");
        List<Product> pRankList = productService.list(productWrapper);
        List<Imgs> iRankList = new ArrayList<>();
        for (Product p : pRankList) {
            if (p.getImgsId() != null) {
                QueryWrapper<Imgs> Imgswrapper = new QueryWrapper<Imgs>()
                        .eq("imgs_id", p.getImgsId()).last("limit 1");
                iRankList.add(imgsService.getOne(Imgswrapper));
            } else {
                iRankList.add(new Imgs());
            }
        }
        return Result.succ(MapUtil.builder()
                .put("pRankList", pRankList)
                .put("iRankList", iRankList)
                .map()
        );
    }


    /**
     * 【游客】查看商品详情页
     */
    @GetMapping("/product/{productId}")
    public Result product(@PathVariable(name = "productId") Long productId) {
        Product product = productService.getById(productId);
        product.setAuthorList(authorService.selectAuthorListByProductId(productId));
        int cmtsCount = cmtService.count(new QueryWrapper<Cmt>().eq("product_id", productId));
        Double avgScore = cmtService.getAvgScoreById(productId);

        Long imgsId = product.getImgsId();
        if (imgsId == null) {
            return Result.succ(MapUtil.builder()
                    .put("product", product)
                    .put("cmtsCount", cmtsCount)
                    .put("avgScore", avgScore)
                    .map()
            );
        }
        // 商品图片集
        List<Imgs> imgs = imgsService.list(new QueryWrapper<Imgs>().eq("imgs_id", imgsId));

        return Result.succ(MapUtil.builder()
                .put("product", product)
                .put("imgs", imgs)
                .put("cmtsCount", cmtsCount)
                .put("avgScore", avgScore)
                .map()
        );
    }

    /**
     * 【用户】通过orderId获取product信息从而进行评论
     */
    @RequiresAuthentication
    @GetMapping("/productByOrderId/{orderId}")
    public Result productByOrderId(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        Product p = productService.getById(order.getProductId());

        Long imgsId = p.getImgsId();
        if (imgsId != null) {
            Imgs imgs = imgsService.getOne(new QueryWrapper<Imgs>()
                    .eq("imgs_id", imgsId).last("limit 1"));
            return Result.succ(MapUtil.builder()
                    .put("product", p)
                    .put("imgs", imgs)
                    .map()
            );
        }
        return Result.succ(MapUtil.builder()
                .put("product", p)
                .map()
        );
    }

    /**
     * 【商家】获取自家商品列表
     */
    @RequiresRoles("manager")
    @RequiresAuthentication
    @GetMapping("/productList")
    public Result productPage() {
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        List<Product> products = productService.list(new QueryWrapper<Product>()
                .eq("store_name", userProfile.getUserNickname())
                .eq("product_flag", 0)
                .orderByDesc("update_time")
        );
        return Result.succ(products);
    }

    /**
     * 【商家】下架商品
     */
    @RequiresRoles("manager")
    @RequiresAuthentication
    @DeleteMapping("/product/{productId}")
    public Result delete(@PathVariable Long productId) {
        productService.update(new Product().setProductFlag(1),
                new QueryWrapper<Product>().eq("product_id", productId));
        return Result.succ(null);
    }

    /**
     * 【商家】更新商品
     */
    @RequiresRoles("manager")
    @RequiresAuthentication
    @PutMapping("/product")
    public Result update(@RequestParam(value = "files", required = false) MultipartFile[] files,
                         @Validated Product product,
                         @RequestParam(value = "authorNameList", required = false)
                                     List<String> authorNameList) throws IOException {
        productAuthorService.setPA(product.getProductId(), authorNameList);

        if (files != null && files.length != 0) {
            Long imgsId = MyFileUtil.uploadFiles(files, "product");
            product.setImgsId(imgsId);
        }
        productService.update(product, new QueryWrapper<Product>()
                .eq("product_id", product.getProductId()));
        return Result.succ(null);
    }

    /**
     * 【商家】新增商品
     */
    @RequiresRoles("manager")
    @RequiresAuthentication
    @PostMapping("/product")
    public Result insert(@RequestParam(value = "files", required = false) MultipartFile[] files,
                         @Validated Product product,
                         @RequestParam(value = "authorNameList", required = false)
                                     List<String> authorNameList) throws IOException {

        if (files != null && files.length != 0) {
            Long imgsId = MyFileUtil.uploadFiles(files, "product");
            product.setImgsId(imgsId);
        }
        UserProfile userProfile = (UserProfile) SecurityUtils.getSubject().getPrincipal();
        product.setStoreName(userProfile.getUserNickname());

        // 手动生成雪花算法ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        Long productId = snowflake.nextId();
        product.setProductId(productId);
        productService.save(product);
        productAuthorService.setPA(productId, authorNameList);

        return Result.succ(null);
    }

}
