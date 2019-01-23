package com.qujiali.springboot.mapper;

import com.qujiali.springboot.entity.SysUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangzhengyu
 * @since 2019-01-14
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    public String UserEnter(@Param("uname") String uname , @Param("upassword") String upassword);
}
