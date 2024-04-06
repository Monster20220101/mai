package com.mai.service;

import com.mai.entity.Cmt;
import com.baomidou.mybatisplus.extension.service.IService;


public interface CmtService extends IService<Cmt> {
    public Double getAvgScoreById(Long productId);
}
