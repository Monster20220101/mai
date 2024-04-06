package com.mai.service.impl;

import com.mai.entity.Order;
import com.mai.mapper.OrderMapper;
import com.mai.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
