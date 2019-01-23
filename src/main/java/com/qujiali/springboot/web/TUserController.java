package com.qujiali.springboot.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qujiali.springboot.common.model.WechatUserInfo;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.*;
import com.qujiali.springboot.entity.TUser;
import com.qujiali.springboot.serviceImpl.TUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ydl
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/tUser")
public class TUserController extends AbstractControllerJson {

    @Resource
    private TUserServiceImpl tUserService;


    private ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    /**
     * 微信小程序登陆
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    @GetMapping(value = "/login")
    @ApiOperation(value = "小程序登陆", produces = MediaType.APPLICATION_JSON_VALUE, response = TUser.class)
    public Object login(@RequestParam String code, @RequestParam String encryptedData, @RequestParam String iv){
        WechatUserInfo wechatUserInfo = getWechatUserInfo(code, encryptedData, iv);
        TUser query = new TUser();
        query.setUnionId(wechatUserInfo.getUnionId());
        EntityWrapper<TUser> wrapper = new EntityWrapper<>();
        wrapper.setEntity(query);
        TUser tUser = tUserService.selectOne(wrapper);
        if(tUser!=null){
            return setSuccessModelMap(new ModelMap(),tUser);
        }else{
            Long id = IdUtils.getId();
            TUser register = new TUser();
            register.setId(id);
            register.setUnionId(wechatUserInfo.getUnionId());
            register.setCreateTime(new Date());
            register.setUserAvator(wechatUserInfo.getAvatarUrl());
            register.setGender(wechatUserInfo.getGender());
            register.setMinniprogramOpenid(wechatUserInfo.getOpenId());
            register.setUserName(wechatUserInfo.getNickName());
            register.setEnable(0);
            tUserService.insertOrUpdate(register);
            return setSuccessModelMap(new ModelMap(),tUserService.selectById(id));
        }
    }



    private WechatUserInfo getWechatUserInfo(String code, String encryptedData, String iv) {
        Optional<WechatUserInfo> wechatUserInfoOptional = wechatUserInfo(code, encryptedData, iv);
        if (!wechatUserInfoOptional.isPresent()) {
            Assert.isTrue(Boolean.FALSE, "GET_WECHAT_INFO_FAIL");
        }
        return wechatUserInfoOptional.get();
    }

    private Optional<WechatUserInfo> wechatUserInfo(String code, String encryptedData, String iv) {
            // 微信服务器的接口
            String url = Constants.WECHATURLHEAD + code + Constants.WECHATURLEDN;
            // 服务端请求URL需要的Spring对象
            RestTemplate restTemplate = new RestTemplate();
            // 调用请求url
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
                // 拿到消息体
                String sessionData = responseEntity.getBody();
                JSONObject json = JSON.parseObject(sessionData);
                // 1.sessionkey取消空格
                String sessionkey = json.get("session_key").toString();
                // 2.openId 取消空格补充"+"
                String userInfoJson = null;
                try {
                    userInfoJson = decrypt(encryptedData, iv, sessionkey);
                } catch (InvalidAlgorithmParameterException e) {
                    return Optional.empty();
                }

                WechatUserInfo wechatUserInfo = null;
                try {
                    wechatUserInfo = objectMapper.readValue(userInfoJson, WechatUserInfo.class);
                } catch (IOException e) {
                    return Optional.empty();
                }
                return Optional.ofNullable(wechatUserInfo);
            }
            return Optional.empty();
    }

        private String decrypt(String encryptedData, String iv, String sessionkey) throws InvalidAlgorithmParameterException {
            String data = encryptedData.replaceAll(" ", "+");
            String ivstr = iv.replaceAll(" ", "+");
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(data), Base64.decodeBase64(sessionkey),
                    Base64.decodeBase64(ivstr));
            return new String(WxPKCS7Encoder.decode(resultByte));

        }



    @GetMapping(value = "/queryUserById")
    @ApiOperation(value = "根据Id查询用户", produces = MediaType.APPLICATION_JSON_VALUE, response = TUser.class)
    public Object queryUserById(@RequestParam Long Id){
        return  setSuccessModelMap(new ModelMap(),tUserService.selectById(Id));
    }

}

