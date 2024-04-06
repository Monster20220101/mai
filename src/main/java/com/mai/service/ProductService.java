package com.mai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mai.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends IService<Product> {
    public List<Product> selectProductListByAuthorId(Long AuthorId);
}
