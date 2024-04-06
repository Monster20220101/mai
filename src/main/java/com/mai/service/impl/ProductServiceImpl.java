package com.mai.service.impl;

import com.mai.entity.Product;
import com.mai.mapper.ProductMapper;
import com.mai.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    public List<Product> selectProductListByAuthorId(Long AuthorId) {
        return productMapper.selectProductListByAuthorId(AuthorId);
    }

}
