package com.mai.mapper;

import com.mai.entity.Cmt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CmtMapper extends BaseMapper<Cmt> {
    @Select("select AVG(cmt_score) from cmt where product_id = #{productId} group by product_id;")
    Double getAvgScoreById(@Param("productId") Long productId);
}
