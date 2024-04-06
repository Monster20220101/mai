package com.mai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mai.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("select p.* from product p " +
            "inner join product_author pa on p.product_id=pa.product_id " +
            "where pa.author_id = #{authorId}")
    List<Product> selectProductListByAuthorId(@Param("authorId") Long authorId);
}
