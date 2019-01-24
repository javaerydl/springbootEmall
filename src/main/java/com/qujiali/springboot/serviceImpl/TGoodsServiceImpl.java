package com.qujiali.springboot.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.entity.GoodsStandard;
import com.qujiali.springboot.entity.TGoods;
import com.qujiali.springboot.mapper.TGoodsMapper;
import com.qujiali.springboot.service.GoodsStandardService;
import com.qujiali.springboot.service.TGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author wyc123
 * @since 2019-01-09
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements TGoodsService {

    @Autowired
    private TGoodsMapper tGoodsMapper;
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private GoodsStandardService goodsStandardService;
    @Autowired
    private TCategoryServiceImpl tCategoryService;

    @Autowired
    private GoodsStandardServiceImpl goodsStandardServiceImpl;


    public Object queryByCategory(Long categoryId){
        List<TGoods> result = tGoodsMapper.selectByCategory(categoryId);
        return result;
    }

    public Object queryGoods(Integer param){
        Integer pageNumber = 1;
        if(param!=null){
            pageNumber = param;
        }
        Page<TGoods> page = new Page<>(pageNumber,10,"id_");
        Page<TGoods> pageInfo = tGoodsService.selectPage(page,
                new EntityWrapper<TGoods>()
                        .eq("enable_",1)
                .orderBy("create_time",false)
        );
        for(TGoods tGoods : pageInfo.getRecords()){
            if(goodsStandardServiceImpl.selectList(new EntityWrapper<GoodsStandard>().eq("goods_id", tGoods.getId())).size()!=0) {
                tGoods.setGoodsStandard(goodsStandardServiceImpl.selectList(new EntityWrapper<GoodsStandard>().eq("goods_id", tGoods.getId())));
            }else{
                tGoods.setGoodsStandard(null);
            }
            if(tCategoryService.selectById(tGoods.getCategoryId())!=null) {
                tGoods.setCategoryName(tCategoryService.selectById(tGoods.getCategoryId()).getName());
            }else{
                tGoods.setCategoryName("类目为空");
            }
        }
    return pageInfo;
    }

    public List<TGoods> selectPageNeed(Long categoryId, Integer pageNumber) {
        Page<Object> page = new Page<>(pageNumber,10,"create_time");
        List<TGoods> result = tGoodsMapper.selectPage(page, new EntityWrapper<TGoods>().eq("category_id", categoryId).ne("enable_", 0));
        return result;
    }
}
