package com.mai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mai.entity.ProductAuthor;

import java.util.List;


public interface ProductAuthorService extends IService<ProductAuthor> {

    void setPA(Long productId, List<String> authorNameList);
}
