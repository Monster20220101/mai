package com.mai.service.impl;

import com.mai.entity.User;
import com.mai.mapper.UserMapper;
import com.mai.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
