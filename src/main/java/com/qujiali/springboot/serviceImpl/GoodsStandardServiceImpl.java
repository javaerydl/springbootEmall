package com.qujiali.springboot.serviceImpl;

import com.qujiali.springboot.entity.GoodsStandard;
import com.qujiali.springboot.mapper.GoodsStandardMapper;
import com.qujiali.springboot.service.GoodsStandardService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品规格价格表 服务实现类
 * </p>
 *
 * @author wyc123
 * @since 2019-01-10
 */
@Service
public class GoodsStandardServiceImpl extends ServiceImpl<GoodsStandardMapper, GoodsStandard> implements GoodsStandardService {
    @Autowired
    private GoodsStandardMapper goodsStandardMapper;

    public List<GoodsStandard> queryByGoodId(Long GoodId){
        List<GoodsStandard> goodsStandards = goodsStandardMapper.queryByGoodsId(GoodId);
        return goodsStandards;
    }

}
