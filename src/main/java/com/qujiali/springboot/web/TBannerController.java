package com.qujiali.springboot.web;


import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.entity.TBanner;
import com.qujiali.springboot.service.TBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * banner预览模块 前端控制器
 * </p>
 *
 * @author wyc123
 * @since 2019-01-09
 */
@RestController
@Api(description = "轮循广告接口")
@RequestMapping("/tBanner")
public class TBannerController extends AbstractControllerJson {

    @Resource
    private TBannerService tBannerService;

    @GetMapping(value = "/query")
    @ApiOperation(value = "查询banner列表")
    public Object query (){
        //banner应该不能超过100个
        Page<TBanner> page = new Page<>(1, 100, "id_");
        return setSuccessModelMap(new ModelMap(),tBannerService.selectPage(page));
    }
}

