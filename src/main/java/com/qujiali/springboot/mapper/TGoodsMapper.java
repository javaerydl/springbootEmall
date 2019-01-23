package com.qujiali.springboot.mapper;

import com.qujiali.springboot.entity.TGoods;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author wyc123
 * @since 2019-01-09
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<TGoods> selectMyNeed();
    List<TGoods> selectByCategory(@Param("categoryId") Long categoryId);
}
