package com.qujiali.springboot.sysweb;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.TCategory;
import com.qujiali.springboot.serviceImpl.TCategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 类目表 前端控制器
 * </p>
 *
 * @author wyc
 * @since 2019-01-11
 */
@RestController
@Api(description = "类目管理")
@RequestMapping("/sys/category")
public class SysCategoryController extends AbstractControllerJson {
    @Resource
    private TCategoryServiceImpl tCategoryService;

    @GetMapping("/query")
    @ApiOperation(value = "查询类目列表", produces = MediaType.APPLICATION_JSON_VALUE, response = TCategory.class)
    public Object query(){
        return setSuccessModelMap(new ModelMap(), tCategoryService.selectList(new EntityWrapper<TCategory>().orderBy("create_time",false)));
    }

    @PutMapping("/insert")
    @ApiOperation(value = "添加类目", produces = MediaType.APPLICATION_JSON_VALUE,response = TCategory.class)
    public Object insert(HttpServletRequest request, @RequestBody TCategory param){
        Assert.notNull(param.getName(),"Category_Name_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setId(IdUtils.getId());
        param.setEnable(1);
        param.setUpdateBy(currentUserId);
        param.setCreateBy(currentUserId);
        Boolean result = tCategoryService.insert(param);
        return setSuccessModelMap(new ModelMap(), result);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新类目", produces = MediaType.APPLICATION_JSON_VALUE, response = TCategory.class)
    public Object update(HttpServletRequest request, @RequestBody TCategory param){
        Assert.notNull(param.getId(), "Category_ID_Empty");
        Assert.notNull(param.getName(), "Category_Name_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setUpdateBy(currentUserId);
        return setSuccessModelMap(new ModelMap(), tCategoryService.updateById(param));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除类目", produces = MediaType.APPLICATION_JSON_VALUE, response = TCategory.class)
    public Object delete(HttpServletRequest request, @RequestParam Long Id){
        Assert.currentUserId(request);
        Assert.notNull(Id, "Category_ID_Empty");
        return setSuccessModelMap(new ModelMap(), tCategoryService.deleteById(Id));
    }


}

