package com.qujiali.springboot.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.Constants;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.CartGoods;
import com.qujiali.springboot.entity.OrderGoods;
import com.qujiali.springboot.entity.UserOrder;
import com.qujiali.springboot.mapper.OrderGoodsMapper;
import com.qujiali.springboot.mapper.UserOrderMapper;
import com.qujiali.springboot.service.UserOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户订单表 服务实现类
 * </p>
 *
 * @author yangdelong123
 * @since 2019-01-10
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private UserOrderMapper userOrderMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private CartGoodsServiceImpl cartGoodsService;

    /**
     * 下单接口
     * @param userOrder
     * @param currentUserId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserOrder submitOrder(UserOrder userOrder, Long currentUserId) {
        //校验
        Assert.isNotBlank(userOrder.getConsigneeAddress(), "Address_is_NULL");
        Assert.isNotBlank(userOrder.getConsigneeName(), "Name_is_Null");
        Assert.isNotBlank(userOrder.getConsigneePhone(), "Phone_is_Null");
        Assert.notNull(userOrder.getPriceTotal(), "Price_is_Null");
        Assert.notNull(userOrder.getGoods(),"GoodsIds_Null");
        //订单入库
        try{
            UserOrder param = new UserOrder();
            Long id = IdUtils.getId();
            param.setId(id);
            param.setUserId(currentUserId);
            param.setConsigneeAddress(userOrder.getConsigneeAddress());
            param.setConsigneeName(userOrder.getConsigneeName());
            param.setConsigneePhone(userOrder.getConsigneePhone());
            param.setPriceTotal(userOrder.getPriceTotal());
            param.setEnable(Constants.SUBMITORDER);
            param.setCreateTime(new Date());
            param.setCreateBy(currentUserId);
            //插入订单表
            super.insert(param);
            //插入订单详情表
            param.setGoods(userOrder.getGoods());
            persistenceToOrderGoods(param);
            //这里处理关于清除购物车内容的逻辑
            dealShoppingCart(currentUserId,userOrder.getGoods());
            //删除购物车里面该物品的
            return super.selectById(id);
        }catch (Exception E){
            throw new  RuntimeException(Constants.Exception_Head+"订单入库异常"+E);
        }
    }

    private void dealShoppingCart(Long currentUserId, List<OrderGoods> goods) {
        try{
            goods.forEach(x->{
                //将购物车已经下单的项置为失效
                CartGoods update = new CartGoods();
                update.setEnable(-1);
                cartGoodsService.update(update,
                        new EntityWrapper<CartGoods>()
                                .eq("user_id", currentUserId)
                                .eq("good_id", x.getGoodsId())
                                .eq("good_standard", x.getGoodStandard())
                                .eq("good_count", x.getGoodCount())
                                .eq("enable_",0)

                );
            });
        }catch (Exception E){
            logger.info("下单后处理购物车项异常"+E);
        }
    }

    /**
     * 下单的同时将订单中的明细内容添加到order_goods表中
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void persistenceToOrderGoods(UserOrder param) {
        try{
            List<OrderGoods> goods = param.getGoods();
            if(goods.size()!=0){
                goods.forEach(x->{
                    x.setId(IdUtils.getId());
                    x.setCreateTime(new Date());
                    x.setEnable(Constants.SUBMITORDER);
                    x.setUserOrderId(param.getId());
                    x.setCreateBy(param.getUserId());
                    orderGoodsMapper.insert(x);
                });
            }
        }catch (Exception e){
            logger.info("插入订单详情异常"+e);
        }
    }

    public List<UserOrder> selectOrderByEnable(Integer status, Integer pageNumber,Long currentUserId) {
        List<UserOrder> submitOrderData = userOrderMapper.selectPage(
                new Page<UserOrder>(1, pageNumber),
                new EntityWrapper<UserOrder>()
                        .eq("enable_", status)
                        .eq("user_id",currentUserId)
        );
        return submitOrderData;
    }

    public  List<UserOrder> selectAllOrders(Long currentUserId,Integer pageNumber) {
        List<UserOrder> submitOrderData = userOrderMapper.selectPage(
                new Page<UserOrder>(1, pageNumber),
                new EntityWrapper<UserOrder>()
                        .eq("user_id",currentUserId)
        );
        return submitOrderData;
    }
}
