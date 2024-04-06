package com.mai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mai.entity.Author;

import java.util.List;


public interface AuthorService extends IService<Author> {
    public List<Author> selectAuthorListByProductId(Long productId);
}
