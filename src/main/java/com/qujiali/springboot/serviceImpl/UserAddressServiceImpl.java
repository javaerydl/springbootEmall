package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.UserAddress;
import com.qujiali.springboot.mapper.UserAddressMapper;
import com.qujiali.springboot.service.UserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户地址 服务实现类
 * </p>
 *
 * @author wyc123
 * @since 2019-01-16
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

}
