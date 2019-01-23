package com.qujiali.springboot.sysweb;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.model.OrderDetail;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.Constants;
import com.qujiali.springboot.common.utils.ObjectUtils;
import com.qujiali.springboot.entity.GoodsStandard;
import com.qujiali.springboot.entity.OrderGoods;
import com.qujiali.springboot.entity.UserOrder;
import com.qujiali.springboot.mapper.OrderGoodsMapper;
import com.qujiali.springboot.mapper.UserOrderMapper;
import com.qujiali.springboot.serviceImpl.GoodsStandardServiceImpl;
import com.qujiali.springboot.serviceImpl.UserOrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author YangDeLong
 * @date 2019/1/11 0011 11:01
 **/
@RestController
@RequestMapping("/sys/userOrder")
@Api(value = "后台管理订单")
public class SysUserOrderController extends AbstractControllerJson {

    @Resource
    private UserOrderServiceImpl userOrderService;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private OrderGoodsMapper orderGoodsMapper;
    @Resource
    private GoodsStandardServiceImpl goodsStandardService;
    /**
     * 确认订单接口,将enable值1->2
     * 1->-1
     * @param param
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "确认订单",notes = "管理员确认订单")
    @Transactional(rollbackFor = Exception.class)
    public Object updateOrder(@RequestBody UserOrder param){
        try{
            Long id = param.getId();
            Integer enable = param.getEnable();
            HashMap<String, Object> returnMap = new HashMap<>(2);
            UserOrder query = userOrderService.selectById(id);
            if(ObjectUtils.isNotNull(query)){
                if(query.getEnable().equals(Constants.SUBMITORDER)){
                    //确认状态
                    query.setEnable(enable);
                    query.setUpdateTime(new Date());
                    boolean success = userOrderService.updateById(query);
                    if(success){
                        returnMap.put("mes","确认订单成功");
                        returnMap.put("code",200);
                        return returnMap;
                    }else {
                        returnMap.put("mes","确认订单失败");
                        returnMap.put("code",200);
                        return returnMap;
                    }
                }else{
                    returnMap.put("mes","订单状态错误,请联系管理员");
                    returnMap.put("code",200);
                    return returnMap;
                }
            }else{
                returnMap.put("mes","订单不存在");
                returnMap.put("code",200);
                return returnMap;
            }
        }catch (Exception e){
            logger.info("确认订单/拒绝订单出现异常"+e);
             throw new RuntimeException("确认订单/拒绝订单出现异常"+e);
        }
    }

    /**
     * 管理员查询全部订单
     * @param pageNumber
     * @return
     */
    @GetMapping("/queryAll")
    @ApiOperation(value = "查询全部订单",notes = "管理员查询全部订单")
    public Object queryAllOrder(@RequestParam Integer pageNumber){
        try{
            if(ObjectUtils.isNull(pageNumber)){
                pageNumber=1;
            }
            List<UserOrder> userOrders = userOrderMapper.selectPage(new Page<UserOrder>(pageNumber, 10),
                    new EntityWrapper<UserOrder>()
                            .orderBy("create_time",false));
            return setSuccessModelMap(new ModelMap(),userOrders);
        }catch (Exception e){
            logger.info("管理员查询订单异常"+e);
            throw  new RuntimeException(Constants.Exception_Head+e);
        }
    }

    /**
     * 管理员根据日期查询订单
     * @param date
     * @return
     */
    @GetMapping(value = "/querybydate")
    @ApiOperation(value = "根据日期查询", notes = "管理员根据日期查询订单(YYYY-MM-DD)")
    public Object queryByDate(@RequestParam Date date){
        Assert.notNull(date, "Search_Date_Empty");
        Date Max = Date.from(LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()).with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        Date Min = Date.from(LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        List<UserOrder> orderList = userOrderService.selectList(new EntityWrapper<UserOrder>().between("create_time",Min,Max));
        return setSuccessModelMap(new ModelMap(), orderList);
    }




    @GetMapping("/detail")
    @ApiOperation(value = "查询订单详情",notes = "管理员确认订单")
    public List<OrderDetail> queryOrderDetail(@RequestParam Long orderId){
        Assert.notNull(orderId,"订单Id为空");
        ArrayList<OrderDetail> resultList = new ArrayList<>();
        try{
            UserOrder userOrder = userOrderMapper.selectById(orderId);
            if(ObjectUtils.isNotNull(userOrder)){
                List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(new EntityWrapper<OrderGoods>().eq("user_order_id", orderId));
                orderGoodsList.forEach(x->{
                    OrderDetail query = new OrderDetail();
                    query.setGoodStandard(x.getGoodStandard());
                    query.setGoodCount(x.getGoodCount());
                    query.setGoodImg(x.getGoodImg());
                    query.setEnable(userOrder.getEnable());
                    query.setCreateTime(userOrder.getCreateTime());
                    query.setAddress(userOrder.getConsigneeAddress());
                    query.setPhone(userOrder.getConsigneePhone());
                    query.setName(userOrder.getConsigneeName());
                    query.setTotal(userOrder.getPriceTotal());
                    GoodsStandard goodsStandard = goodsStandardService.selectOne(new EntityWrapper<GoodsStandard>().eq("goods_id", x.getGoodsId()).eq("standard", x.getGoodStandard()));
                    if(ObjectUtils.isNotNull(goodsStandard)){
                        query.setPrice(goodsStandard.getPrice());
                    }
                    resultList.add(query);
                });
                return resultList;
            }
        }catch (Exception E){
                logger.info("后台管理查询订单详情异常");
        }
        return  resultList;
    }
}
