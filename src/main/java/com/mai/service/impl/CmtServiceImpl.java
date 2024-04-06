package com.mai.service.impl;

import com.mai.entity.Cmt;
import com.mai.mapper.CmtMapper;
import com.mai.service.CmtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CmtServiceImpl extends ServiceImpl<CmtMapper, Cmt> implements CmtService {
    @Autowired
    private CmtMapper cmtMapper;

    public Double getAvgScoreById(Long productId) {
        Double avgScore = cmtMapper.getAvgScoreById(productId);
        return avgScore==null ? 0 : avgScore;
    }
}
