package com.qujiali.springboot.sysweb;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author YangDeLong
 * @date 2018/12/18 0018 16:40
 **/
@Controller
@RequestMapping
public class HelloController {
    @ApiOperation(value = "前后端分离返回json实例",notes = "这个默认请求出来的为json不指定页面")
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public Object helloWord(){
        String str="index";
        return str;
    }
}
