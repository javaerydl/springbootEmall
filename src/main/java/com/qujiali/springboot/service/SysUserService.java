package com.qujiali.springboot.service;

import com.qujiali.springboot.entity.SysUser;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangzhengyu
 * @since 2019-01-14
 */
public interface SysUserService extends IService<SysUser> {
    public String UserEnter(@Param("uname") String uname, @Param("upassword") String upassword);


}
