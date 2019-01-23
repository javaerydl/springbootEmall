package com.qujiali.springboot.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.qujiali.springboot.common.model.Lock;

public interface LockMapper extends BaseMapper<Lock> {

	void cleanExpiredLock();

}
