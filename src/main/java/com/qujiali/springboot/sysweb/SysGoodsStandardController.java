package com.qujiali.springboot.sysweb;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.GoodsStandard;
import com.qujiali.springboot.entity.TGoods;
import com.qujiali.springboot.serviceImpl.GoodsStandardServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.annotation.Id;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * <p>
 * 商品规格价格表 前端控制器
 * </p>
 *
 * @author wyc
 * @since 2019-01-11
 */
@Controller
@Api(description = "商品规格价格管理")
@RequestMapping("/sys/goodsStandard")
public class SysGoodsStandardController extends AbstractControllerJson {
    @Resource
    private GoodsStandardServiceImpl goodsStandardService;

    @GetMapping(value = "/querybyid")
    @ApiOperation(value = "查询规格价格表", produces = MediaType.APPLICATION_JSON_VALUE, response = GoodsStandard.class)
    public Object query(HttpServletRequest request, @RequestParam Long Id){
        Assert.notNull(Id,"ID为空");
        return setSuccessModelMap(new ModelMap(), goodsStandardService.selectList(new EntityWrapper<GoodsStandard>().eq("goods_id", Id).orderBy("create_time",false)));
    }

    @PutMapping(value = "/insert")
    @ApiOperation(value = "插入规格价格", produces = MediaType.APPLICATION_JSON_VALUE,response = GoodsStandard.class)
    public Object insert(HttpServletRequest request, @RequestBody GoodsStandard param){
        Assert.notNull(param.getGoodsId(),"Goods_ID_Empty");
        Assert.notNull(param.getStandard(),"Goods_Standard_Empty");
        Assert.notNull(param.getPrice(),"Goods_Price_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setId(IdUtils.getId());
        param.setCreateBy(currentUserId);
        param.setUpdateBy(currentUserId);
        return setSuccessModelMap(new ModelMap(), goodsStandardService.insert(param));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改规格价格", produces = MediaType.APPLICATION_JSON_VALUE, response = GoodsStandard.class)
    public Object update(@RequestBody GoodsStandard param){
        Assert.notNull(param.getGoodsId(), "Goods_ID_Empty");
        Assert.notNull(param.getStandard(), "Goods_Standard_Empty");
        Assert.notNull(param.getPrice(), "Goods_Price_Empty");
        return setSuccessModelMap(new ModelMap(), goodsStandardService.updateById(param));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除规格价格",produces = MediaType.APPLICATION_JSON_VALUE, response = GoodsStandard.class)
    public Object delete(@RequestParam Long Id){
        Assert.notNull(Id, "ID为空");
        return setSuccessModelMap(new ModelMap(), goodsStandardService.deleteById(Id));
    }


}

