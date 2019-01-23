package com.qujiali.springboot.mapper;

import com.qujiali.springboot.entity.GoodsStandard;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 商品规格价格表 Mapper 接口
 * </p>
 *
 * @author wyc123
 * @since 2019-01-10
 */
public interface GoodsStandardMapper extends BaseMapper<GoodsStandard> {
    List<GoodsStandard> queryByGoodsId(@Param("GoodsId") Long GoodsId);
}
