package com.qujiali.springboot.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.DataUtil;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.GoodsStandard;
import com.qujiali.springboot.entity.TGoods;
import com.qujiali.springboot.entity.TUser;
import com.qujiali.springboot.mapper.TGoodsMapper;
import com.qujiali.springboot.service.TGoodsService;
import com.qujiali.springboot.serviceImpl.GoodsStandardServiceImpl;
import com.qujiali.springboot.serviceImpl.TCategoryServiceImpl;
import com.qujiali.springboot.serviceImpl.TGoodsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author wyc123
 * @since 2019-01-09
 */
@RestController
@Api(description = "商品接口")
@RequestMapping("/tGoods")
public class TGoodsController extends AbstractControllerJson {

    @Resource
    private TGoodsServiceImpl tGoodsService;
    @Resource
    private GoodsStandardServiceImpl goodsStandardServiceImpl;
    @Resource
    private TCategoryServiceImpl tCategoryService;


    @GetMapping(value = "/querybyid")
    @ApiOperation(value = "根据ID查询商品", produces = MediaType.APPLICATION_JSON_VALUE, response = TGoods.class)
    public Object queryGoodsById(@RequestParam Long Id){
        Assert.notNull(Id,"Id为空");
        TGoods tGoods = new TGoods();
        tGoods = tGoodsService.selectById(Id);
        //填充商品规格及价格
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
        return setSuccessModelMap(new ModelMap(),tGoods);
    }

    @GetMapping(value = "/querybycondition")
    @ApiOperation(value = "模糊查询测试", produces = MediaType.APPLICATION_JSON_VALUE, response = TGoods.class)
    public Object search(@RequestParam String condition){
        Assert.notNull(condition,"条件为空");
        List<TGoods> querList = tGoodsService.selectList(new EntityWrapper<TGoods>().like("name", condition).ne("enable_", 0));
        for(TGoods tGoods : querList){
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
        return setSuccessModelMap(new ModelMap(), querList);
    }

    @GetMapping(value = "/querybycategory")
    @ApiOperation(value = "根据类目查询（传入类目ID）", produces = MediaType.APPLICATION_JSON_VALUE, response = TGoods.class)
    public Object queryByCategory(@RequestParam Long categoryId, @RequestParam Integer pageNumber){
        Assert.notNull(categoryId,"类目ID为空");
        List<TGoods> querList= tGoodsService.selectPageNeed(categoryId,pageNumber);
        for(TGoods tGoods : querList){
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
        return setSuccessModelMap(new ModelMap(), querList);
    }

    @GetMapping(value = "/query")
    @ApiOperation(value = "分页查询所有商品列表(分页查询，页大小固定为10，只需传页数)", produces = MediaType.APPLICATION_JSON_VALUE, response = TGoods.class)
    public Object query(@RequestParam Integer pageNumber){
        Assert.notNull(pageNumber,"页数为空");
        //封装请求参数
        Page<TGoods> page = new Page<>(pageNumber,10,"id_");
        //获取前端请求页数
        return setSuccessModelMap(new ModelMap(), tGoodsService.queryGoods(pageNumber));
    }

    @GetMapping(value = "/queryHotSale")
    @ApiOperation(value = "查询热销商品", produces = MediaType.APPLICATION_JSON_VALUE, response = TGoods.class)
    public Object queryHotSale(@RequestParam Integer pageNumber){
        Assert.notNull(pageNumber,"页数为空");
        Page<TGoods> page = new Page<>(pageNumber,10,"id_");
        Page<TGoods> pageInfo = tGoodsService.selectPage(page,new EntityWrapper<TGoods>().eq("enable_", 2));
        for(TGoods tGoods: pageInfo.getRecords()){
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
        return setSuccessModelMap(new ModelMap(),pageInfo);
    }

}

