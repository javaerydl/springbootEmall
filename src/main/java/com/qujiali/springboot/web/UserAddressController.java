package com.qujiali.springboot.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.IdUtils;
import com.qujiali.springboot.entity.TUser;
import com.qujiali.springboot.entity.UserAddress;
import com.qujiali.springboot.service.UserAddressService;
import com.qujiali.springboot.serviceImpl.UserAddressServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 用户地址 前端控制器
 * </p>
 *
 * @author wyc123
 * @since 2019-01-16
 */
@Controller
@Api(description = "用户地址接口")
@RequestMapping("/userAddress")
public class UserAddressController extends AbstractControllerJson {
    @Resource
    private UserAddressServiceImpl userAddressService;

    @GetMapping(value = "/querylistbyuserid")
    @ApiOperation(value = "查询用户地址" , produces = MediaType.APPLICATION_JSON_VALUE, response = UserAddress.class)
    public Object queryListByUserId(HttpServletRequest request){
        Long userId = Assert.currentUserId(request);
        Assert.notNull(userId, "User_ID_Empty");
        return setSuccessModelMap(new ModelMap(), userAddressService.selectList(new EntityWrapper<UserAddress>().eq("user_id", userId).orderBy("create_time",false)));
    }

    @GetMapping(value = "/querybyid")
    @ApiOperation(value = "查询单条地址信息", produces = MediaType.APPLICATION_JSON_VALUE, response = UserAddress.class)
    public Object queryById(@RequestParam Long Id){
        Assert.notNull(Id, "Address_ID_Empty");
        return setSuccessModelMap(new ModelMap(), userAddressService.selectById(Id));
    }

    @PutMapping(value = "/insert")
    @ApiOperation(value = "添加用户地址", produces = MediaType.APPLICATION_JSON_VALUE, response = UserAddress.class)
    public Object insert(HttpServletRequest request, @RequestBody UserAddress param){
        Assert.notNull(param.getName(),"User_Name_Empty");
        Assert.notNull(param.getPhone(),"User_Phone_empty");
        Assert.notNull(param.getAddress(),"User_Address_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setId(IdUtils.getId());
        param.setUserId(currentUserId);
        param.setCreateBy(currentUserId);
        param.setUpdateBy(currentUserId);
        param.setEnable(1);
        param.setCreateTime(new Date());
        boolean insert = userAddressService.insert(param);
        return setSuccessModelMap(new ModelMap(), insert);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改用户地址", produces = MediaType.APPLICATION_JSON_VALUE, response = UserAddress.class)
    public Object update(HttpServletRequest request, @RequestBody UserAddress param){
        Assert.notNull(param.getName(),"User_Name_Empty");
        Assert.notNull(param.getPhone(),"User_Phone_empty");
        Assert.notNull(param.getAddress(),"User_Address_Empty");
        Long currentUserId = Assert.currentUserId(request);
        param.setUpdateBy(currentUserId);
        return setSuccessModelMap(new ModelMap(), userAddressService.updateById(param));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除用户地址",produces = MediaType.APPLICATION_JSON_VALUE,response = UserAddress.class)
    public Object delete(@RequestParam Long Id){
        Assert.notNull(Id,"Address_ID_Empty");
        return setSuccessModelMap(new ModelMap(), userAddressService.deleteById(Id));
    }
}

