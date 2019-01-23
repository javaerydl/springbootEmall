package com.qujiali.springboot.web;

import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Assert;
import com.qujiali.springboot.common.utils.VerificationUtils;
import com.qujiali.springboot.serviceImpl.SysUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 *  登录页面 前端控制器
 * </p>
 *
 * @author wangzhengyu
 * @since 2019-01-14
 * @SpringBootApplication   本程序的入口
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends AbstractControllerJson {
    @Resource
    private SysUserServiceImpl sysUserServiceImpl;
    /**
     * 登录
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "登录页面")
    public Object UserEnter(@RequestParam String uname,@RequestParam String upassword,@RequestParam String code,
                            HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object randCheckCode = session.getAttribute("randCheckCode");
        Assert.isNotBlank(code,"验证码为空，请输入验证码");
        if (randCheckCode==null){
            request.setAttribute("erramage","验证码已失效，请刷新获取");
            throw new IllegalArgumentException("验证码已失效，请刷新获取");
        }
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        LocalDateTime localDateTime = (LocalDateTime) session.getAttribute("codetime");
        long past = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if ((now-past)/1000/60>5){
            request.setAttribute("erramage","验证码已过期，请刷新获取");
            throw new IllegalArgumentException("验证码已过期，请刷新获取");
        }else if (code.equalsIgnoreCase(randCheckCode.toString() )) {
            String userEnter = sysUserServiceImpl.UserEnter(uname, upassword);
            if (StringUtils.isBlank(userEnter)) {
                throw new IllegalArgumentException("用户名或者密码错误!");
            }
            return setSuccessModelMap(new ModelMap(),userEnter);
        } else {
            throw new IllegalArgumentException("验证码错误");
        }

    }

    @GetMapping("getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置不缓存图片
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "No-cache");
            response.setDateHeader("Expires", 0);
            //指定生成的响应图片,一定不能缺少这句话,否则错误.
            response.setContentType("image/jpeg");
            Object[] picture = VerificationUtils.picture();
            HttpSession session = request.getSession(true);
            // 把当前生成的验证码存在session中，当用户输入后进行对比
            session.setAttribute("randCheckCode", picture[0]);
            RenderedImage image = (RenderedImage) picture[1];
            //输出图片
            ImageIO.write(image, "JPEG", response.getOutputStream());
            session.removeAttribute("codetime");
            session.setAttribute("codetime", LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Exception错误，异常为："+e);
        }
    }
}

