package com.mai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mai.entity.Author;
import com.mai.mapper.AuthorMapper;
import com.mai.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {
    @Autowired
    private AuthorMapper authorMapper;

    public List<Author> selectAuthorListByProductId(Long productId) {
        return authorMapper.selectAuthorListByProductId(productId);
    }
}
