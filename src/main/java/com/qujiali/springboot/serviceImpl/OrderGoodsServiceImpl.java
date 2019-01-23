package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.OrderGoods;
import com.qujiali.springboot.mapper.OrderGoodsMapper;
import com.qujiali.springboot.service.OrderGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author yangdelong123
 * @since 2019-01-10
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements OrderGoodsService {

}
