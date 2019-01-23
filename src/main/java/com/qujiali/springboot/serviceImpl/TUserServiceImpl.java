package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.TUser;
import com.qujiali.springboot.mapper.TUserMapper;
import com.qujiali.springboot.service.TUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ydl123
 * @since 2019-01-08
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}
