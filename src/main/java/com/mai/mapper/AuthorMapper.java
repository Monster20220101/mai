package com.mai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mai.entity.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorMapper extends BaseMapper<Author> {
    @Select("select a.* from author a " +
            "inner join product_author pa on a.author_id=pa.author_id " +
            "where pa.product_id = #{productId}")
    List<Author> selectAuthorListByProductId(@Param("productId") Long productId);
}
