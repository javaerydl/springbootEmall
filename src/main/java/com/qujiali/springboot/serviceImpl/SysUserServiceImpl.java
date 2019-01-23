package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.entity.SysUser;
import com.qujiali.springboot.mapper.SysUserMapper;
import com.qujiali.springboot.service.SysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;



/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangzhengyu
 * @since 2019-01-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public String UserEnter(String uname, String upassword) {
        //判断名字格式是否正确
        Assert.userName(uname);
        //判断密码是否为空
        Assert.isNotBlank(upassword,"Password为空");
        String userEnter = sysUserMapper.UserEnter(uname, upassword);
        return userEnter;
    }
}