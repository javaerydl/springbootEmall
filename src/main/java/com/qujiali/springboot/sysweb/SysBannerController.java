package com.qujiali.springboot.sysweb;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.TBanner;
import com.qujiali.springboot.service.TBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * <p>
 * banner预览模块 前端控制器
 * </p>
 *
 * @author wyc
 * @since 2019-01-11
 */
@RestController
@Api(description = "轮循广告管理")
@RequestMapping("/sys/banner")
public class SysBannerController extends AbstractControllerJson {

    @Resource
    private TBannerService tBannerService;

    @GetMapping(value = "/query")
    @ApiOperation(value = "查询banner列表")
    public Object query (){
        Page<TBanner> page = new Page<>(1, 10, "id_");
        return setSuccessModelMap(new ModelMap(),tBannerService.selectList(new EntityWrapper<TBanner>().orderBy("create_time",false)));
    }

    @PutMapping(value = "insert")
    @ApiOperation(value = "插入banner", produces = MediaType.APPLICATION_JSON_VALUE, response = TBanner.class)
    public Object insert(HttpServletRequest request, @RequestBody TBanner param){
        Assert.notNull(param.getPictureUrl(),"Banner_Pic_Url_Empty");
        Assert.notNull(param.getLinkUrl(), "Banner_Link_Url_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setId(IdUtils.getId());
        param.setEnable(1);
        param.setCreateBy(currentUserId);
        param.setUpdateBy(currentUserId);
        return setSuccessModelMap(new ModelMap(), tBannerService.insert(param));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除Banner", produces = MediaType.APPLICATION_JSON_VALUE, response = TBanner.class)
    public Object delete(@RequestParam Long Id){
        Assert.notNull(Id, "Banner_ID_Empty");
        return setSuccessModelMap(new ModelMap(), tBannerService.deleteById(Id));
    }

}

