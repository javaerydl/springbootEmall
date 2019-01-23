package com.qujiali.springboot.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.TCategory;
import com.qujiali.springboot.entity.TGoods;
import com.qujiali.springboot.serviceImpl.TCategoryServiceImpl;
import com.qujiali.springboot.serviceImpl.TGoodsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * <p>
 * 类目表 前端控制器
 * </p>
 *
 * @author wyc123
 * @since 2019-01-10
 */
@RestController
@Api(description = "类目接口")
@RequestMapping("/tCategory")
public class TCategoryController extends AbstractControllerJson {
    @Resource
    private TCategoryServiceImpl tCategoryService;

    @GetMapping("/query")
    @ApiOperation(value = "查询类目列表", produces = MediaType.APPLICATION_JSON_VALUE, response = TCategory.class)
    public Object query(){
        Page<TCategory> page = new Page<>(1,100,"id_");
        return setSuccessModelMap(new ModelMap(), tCategoryService.selectPage(page));
    }

}

