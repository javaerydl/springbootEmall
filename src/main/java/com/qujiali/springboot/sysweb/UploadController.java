package com.qujiali.springboot.sysweb;

import java.io.*;
import java.util.UUID;


import com.qiniu.storage.UploadManager;
import com.qujiali.springboot.common.support.AbstractControllerJson;
import com.qujiali.springboot.common.utils.Constants;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;

import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;


@RestController
@RequestMapping(value = "/qiniu")
public class UploadController extends AbstractControllerJson {

    @PostMapping (value = "/upload")
    public String imgUrl(MultipartFile img) throws IOException{
            //方法一 获取相对应的流
            InputStream stream = img.getInputStream();
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone0());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            String accessKey = Constants.ACCESSKEY;
            String secretKey = Constants.SECRECTKEY;
            String bucket = Constants.BUCKET;
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = UUID.randomUUID().toString().replaceAll("", "");
            try {
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);
                Response response = uploadManager.put(stream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            } catch (Exception ex) {
                return "fail";
            }
        return Constants.QINIUPRO+key;
    }



    /**
     * 七牛上传Token
     */
    @ApiOperation(value = "获取七牛上传token", produces = MediaType.APPLICATION_JSON_VALUE, response = Object.class)
    @GetMapping("/token")
    public Object qiniuUploadToken(){
        {
            try {
                // 从配置文件中读取需要的信息
                String accessKey = Constants.ACCESSKEY;
                String secretKey = Constants.SECRECTKEY;
                Auth create = Auth.create(accessKey, secretKey);
                String bucket=Constants.BUCKET;
                String uploadToken = create.uploadToken(bucket);
                return setSuccessModelMap(new ModelMap(),uploadToken);
            } catch (Exception e) {
                logger.info("生成上传七牛token失败");
                throw new RuntimeException();
            }
        }
    }


}