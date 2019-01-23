package com.qujiali.springboot.web;


import com.qujiali.springboot.common.support.AbstractControllerJson;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 商品规格价格表 前端控制器
 * </p>
 *
 * @author wyc123
 * @since 2019-01-10
 */
@Controller
@Api(description = "商品规格价格接口")
@RequestMapping("/goodsStandard")
public class GoodsStandardController extends AbstractControllerJson {

}

