package com.mai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mai.entity.Author;
import com.mai.entity.ProductAuthor;
import com.mai.mapper.AuthorMapper;
import com.mai.mapper.ProductAuthorMapper;
import com.mai.service.ProductAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductAuthorImpl extends ServiceImpl<ProductAuthorMapper, ProductAuthor> implements ProductAuthorService {
    @Autowired
    AuthorMapper authorMapper;
    @Autowired
    ProductAuthorMapper productAuthorMapper;

    @Override
    public void setPA(Long productId, List<String> authorNameList) {
        System.err.println(authorNameList);
        List<Author> oldAuthorList = authorMapper.selectAuthorListByProductId(productId);
        List<Author> deleteAuthorList = authorMapper.selectAuthorListByProductId(productId);

        if(authorNameList == null) {
            deletePA(productId, deleteAuthorList);
            return;
        }

        for(String authorName : authorNameList) {
            Author author = authorMapper.selectOne(new QueryWrapper<Author>()
                    .eq("author_name", authorName));
            if(author == null) {
                // mysql的author表里没记录
                author = new Author();
                author.setAuthorName(authorName);
                authorMapper.insert(author);
                author = authorMapper.selectOne(new QueryWrapper<Author>()
                        .eq("author_name", authorName));
                productAuthorMapper.insert(new ProductAuthor(productId,author.getAuthorId()));

            } else {
                // mysql的author表里有记录
                /**
                 * old: a b
                 * new: b c
                 * b:contain
                 * c:not contain
                 * 然后要在循环外处理a
                 */
                if (oldAuthorList.contains(author)) {
                    deleteAuthorList.remove(author);
                } else {
                    productAuthorMapper.insert(new ProductAuthor(productId,author.getAuthorId()));
                }
            }
        }

        // 在循环外处理a
        deletePA(productId, deleteAuthorList);
    }

    private void deletePA(Long productId, List<Author> deleteAuthorList) {
        for(Author a : deleteAuthorList) {
            productAuthorMapper.delete(new QueryWrapper<ProductAuthor>()
                    .eq("product_id", productId)
                    .eq("author_id", a.getAuthorId())
            );
        }
    }
}
