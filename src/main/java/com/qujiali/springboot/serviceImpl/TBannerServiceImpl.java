package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.TBanner;
import com.qujiali.springboot.mapper.TBannerMapper;
import com.qujiali.springboot.service.TBannerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * banner预览模块 服务实现类
 * </p>
 *
 * @author wyc123
 * @since 2019-01-09
 */
@Service
public class TBannerServiceImpl extends ServiceImpl<TBannerMapper, TBanner> implements TBannerService {

}
