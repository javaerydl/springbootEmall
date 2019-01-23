package com.qujiali.springboot.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.entity.OrderGoods;
import com.qujiali.springboot.entity.UserOrder;
import com.qujiali.springboot.serviceImpl.OrderGoodsServiceImpl;
import com.qujiali.springboot.serviceImpl.UserOrderServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户订单表 前端控制器
 * </p>
 *
 * @author yangdelong123
 * @since 2019-01-10
 */
@RestController
@RequestMapping("/userOrder")
public class UserOrderController extends AbstractControllerJson {

    @Autowired
    private UserOrderServiceImpl userOrderService;

    @Autowired
    private OrderGoodsServiceImpl orderGoodsService;

    /**
     * 确认下单的参数
     * @param request
     * @param userOrder
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "下单", produces = MediaType.APPLICATION_JSON_VALUE, response = UserOrder.class)
    public Object submitOrder(HttpServletRequest request, @RequestBody UserOrder userOrder){
        Long currentUserId = Assert.currentUserId(request);
        Assert.notNull(userOrder,"USE_ORDER_ISNULL");
        UserOrder order = userOrderService.submitOrder(userOrder,currentUserId);
        return setSuccessModelMap(new ModelMap(),order);
    }

    /**
     * 查询订单状态，根据前端传递值来查询订单状态
     * @param request
     * @param status
     * @return
     */
    @GetMapping("/queryOrder")
    @ApiOperation(value = "查询订单", produces = MediaType.APPLICATION_JSON_VALUE, response = UserOrder.class)
    public Object queryMyOder(HttpServletRequest request,@RequestParam(required = false)  Integer status,@RequestParam Integer pageNumber){
        Assert.notNull(pageNumber,"PageNumber_Can't_Be_Null");
        Long currentUserId = Assert.currentUserId(request);
        List<UserOrder> result=new ArrayList<>();
        //等待付款
        if(status!=null){
            result = userOrderService.selectOrderByEnable(status,pageNumber,currentUserId);
            result.forEach(x->{
                List<OrderGoods> queryData = orderGoodsService.selectList(new EntityWrapper<OrderGoods>().eq("user_order_id", x.getId()));
                x.setGoods(queryData);
            });
        }else{
            result= userOrderService.selectAllOrders(currentUserId,pageNumber);
            result.forEach(x->{
                List<OrderGoods> queryData = orderGoodsService.selectList(new EntityWrapper<OrderGoods>().eq("user_order_id", x.getId()));
                x.setGoods(queryData);
            });
        }
        return result;
    }
}

