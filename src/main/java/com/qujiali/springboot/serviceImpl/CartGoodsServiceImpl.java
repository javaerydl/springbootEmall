package com.qujiali.springboot.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.entity.CartGoods;
import com.qujiali.springboot.mapper.CartGoodsMapper;
import com.qujiali.springboot.service.CartGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author yangdelong123
 * @since 2019-01-14
 */
@Service
public class CartGoodsServiceImpl extends ServiceImpl<CartGoodsMapper, CartGoods> implements CartGoodsService {
    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    public List<CartGoods> selectMyPage(Integer pageNumber,Long currentUserId) {
        List<CartGoods> resultList = cartGoodsMapper.selectPage(
                new Page<CartGoods>(pageNumber, 10), new EntityWrapper<CartGoods>()
                .eq("user_id", currentUserId)
                .eq("enable_",0)
        );
        resultList.forEach(x->x.setSelected(true));
        return resultList;
    }
}
