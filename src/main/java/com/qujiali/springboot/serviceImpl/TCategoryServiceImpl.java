package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.TCategory;
import com.qujiali.springboot.mapper.TCategoryMapper;
import com.qujiali.springboot.service.TCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author wyc123
 * @since 2019-01-10
 */
@Service
public class TCategoryServiceImpl extends ServiceImpl<TCategoryMapper, TCategory> implements TCategoryService {

}
