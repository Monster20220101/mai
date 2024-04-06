package com.mai.service.impl;

import com.mai.entity.Address;
import com.mai.mapper.AddressMapper;
import com.mai.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
