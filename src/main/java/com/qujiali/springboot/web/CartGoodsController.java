package com.qujiali.springboot.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.common.utils.ObjectUtils;
import com.qujiali.springboot.entity.CartGoods;
import com.qujiali.springboot.serviceImpl.CartGoodsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author yangdelong123
 * @since 2019-01-14
 */
@Controller
@RequestMapping("/cartGoods")
@Api(description = "购物车模块")
public class CartGoodsController extends AbstractControllerJson {

    @Autowired
    private CartGoodsServiceImpl cartGoodsService;


    @PostMapping("/addCart")
    @ApiOperation(value = "添加购物车", produces = MediaType.APPLICATION_JSON_VALUE, response = CartGoods.class)
    public Object addCart(HttpServletRequest request, @RequestBody CartGoods cartGoods){
        Assert.notNull(cartGoods,"Cart_Goods_ISNULL");
        Assert.notNull(cartGoods.getGoodId(),"Cart_Good_ISNULL");
        Assert.notNull(cartGoods.getGoodPrice(),"Good_Price_ISNULL");
        Assert.notNull(cartGoods.getGoodCount(),"Good_Count_ISNULL");
        Assert.isNotBlank(cartGoods.getGoodImg(),"Good_img_ISNULL");
        Assert.isNotBlank(cartGoods.getGoodStandard(),"Good_Standard_ISNULL");
        Long currentUserId = Assert.currentUserId(request);
        //先查询商品是否存在于我的购物车中
        CartGoods querResult = cartGoodsService.selectOne(new EntityWrapper<CartGoods>()
                .eq("user_id", currentUserId)
                .eq("good_id", cartGoods.getGoodId())
                .eq("good_standard",cartGoods.getGoodStandard())
                .eq("enable_",0)
        );
        if(ObjectUtils.isNotNull(querResult)){
            int count = querResult.getGoodCount() + cartGoods.getGoodCount();
            querResult.setGoodCount(count);
            boolean b = cartGoodsService.updateById(querResult);
            return  setSuccessModelMap(new ModelMap(),b);
        }
        CartGoods insertCart = new CartGoods();
        insertCart.setUserId(currentUserId);
        insertCart.setId(IdUtils.getId());
        insertCart.setGoodId(cartGoods.getGoodId());
        insertCart.setGoodPrice(cartGoods.getGoodPrice());
        insertCart.setGoodCount(cartGoods.getGoodCount());
        insertCart.setGoodImg(cartGoods.getGoodImg());
        insertCart.setCreateTime(new Date());
        insertCart.setGoodStandard(cartGoods.getGoodStandard());
        insertCart.setEnable(0);
        boolean insert = cartGoodsService.insert(insertCart);
        return  setSuccessModelMap(new ModelMap(),insert);
    }

    /**
     * 查询我的购物车
     * @param request
     * @param pageNumber
     * @return
     */
    @GetMapping("/queryCart")
    @ApiOperation(value = "查询我的购物车", produces = MediaType.APPLICATION_JSON_VALUE, response = CartGoods.class)
    public Object querMyCart(HttpServletRequest request, @RequestParam Integer pageNumber){
        Long currentUserId = Assert.currentUserId(request);
        List<CartGoods> result= cartGoodsService.selectMyPage(pageNumber,currentUserId);
        return setSuccessModelMap(new ModelMap(),result);
    }

    @DeleteMapping("/deleteCart")
    @ApiOperation(value = "删除我的购物项", produces = MediaType.APPLICATION_JSON_VALUE, response = CartGoods.class)
    public Object deleteMyCart(HttpServletRequest request,@RequestBody CartGoods cartGoods){
        Long currentUserId = Assert.currentUserId(request);
        Assert.notNull(cartGoods,"CartGoods_isNULL");
        Assert.notNull(cartGoods.getId(),"CartGoodsID_IsNull");
        return  setSuccessModelMap(new ModelMap(),cartGoodsService.deleteById(cartGoods.getId()));
    }

}

