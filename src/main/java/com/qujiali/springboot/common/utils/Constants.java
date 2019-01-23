package com.qujiali.springboot.common.utils;


import com.qujiali.springboot.common.cache.CacheKey;

import java.util.Map;

/**
 * 常量表
 * 
 
 * @version $Id: Constants.java, v 0.1 2014-2-28 上午11:18:28  Exp $
 */
public interface Constants {
    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    /**
     * 七牛云需要
     */
    static final String QINIUPRO="http://pld4hurrv.bkt.clouddn.com/";
    static final String ACCESSKEY="IZbNff76iTgepiXuNcgeso1aiLYUApTMsfbABiQZ";
    static final String SECRECTKEY = "evKnsEVEs6LxQnGTaA8m3DNjx34GlqqgZBgTtVyA";
    static final String BUCKET = "emall-wechatmini-good";

    static final String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";
    /** 缓存键值 */
    static final Map<Class<?>, CacheKey> cacheKeyMap = InstanceUtil.newHashMap();
    /** 操作名称 */
    static final String OPERATION_NAME = "OPERATION_NAME";
    /** 客户端语言 */
    static final String USERLANGUAGE = "userLanguage";
    /** 客户端主题 */
    static final String WEBTHEME = "webTheme";
    /** 当前用户 */
    static final String CURRENT_USER = "CURRENT_USER";
    /** 客户端信息 */
    static final String USER_AGENT = "USER-AGENT";
    /** 客户端信息 */
    static final String USER_IP = "USER_IP";
    /** 登录地址 */
    static final String LOGIN_URL = "/login.html";
    /** 缓存命名空间 */
    static final String CACHE_NAMESPACE = "qujiali:";
    /** 缓存命名空间 */
    static final String SYSTEM_CACHE_NAMESPACE = "S:qujiali:";
    /** 缓存命名空间 */
    static final String CACHE_NAMESPACE_LOCK = "L:qujiali:";
    /** 上次请求地址 */
    static final String PREREQUEST = CACHE_NAMESPACE + "PREREQUEST";
    /** 上次请求时间 */
    static final String PREREQUEST_TIME = CACHE_NAMESPACE + "PREREQUEST_TIME";
    /** 非法请求次数 */
    static final String MALICIOUS_REQUEST_TIMES = CACHE_NAMESPACE + "MALICIOUS_REQUEST_TIMES";
    /** 在线用户数量 */
    static final String ALLUSER_NUMBER = SYSTEM_CACHE_NAMESPACE + "ALLUSER_NUMBER";
    /** TOKEN */
    static final String TOKEN_KEY = SYSTEM_CACHE_NAMESPACE + "TOKEN_KEY";
    /** shiro cache */
    static final String REDIS_SHIRO_CACHE = SYSTEM_CACHE_NAMESPACE + "SHIRO-CACHE:";
    /** SESSION */
    static final String REDIS_SHIRO_SESSION = SYSTEM_CACHE_NAMESPACE + "SHIRO-SESSION:";
    /** CACHE */
    static final String MYBATIS_CACHE = "D:qujiali:MYBATIS:";
    /** 默认数据库密码加密key */
    static final String DB_KEY = "90139119";
    /** 临时目录 */
    static final String TEMP_DIR = "/temp/";

    static final String SMS_REGISTER_CODE = "sms:register:code:";

    static final String SMS_RESET_PHONE_CODE = "sms:reset:phone:code:";

    static final String SMS_WECHAT_BIND_CODE = "sms:wechat:bind:code:";

    static final String SMS_FORGET_PASSWORD_CODE = "sms:forget:password:code:";

    static final String USER_ROLE = "user:role:";

    static final Integer CODE_VALID_SECOND = 180;

    static final String REGISTER_CODE_COUNT = "REGISTER_CODE_COUNT:";

    static final Integer REGISTER_CODE_COUNT_LIMIT = 5;

    static final String RESET_PHONE_CODE_COUNT = "RESET_PHONE_CODE_COUNT:";

    static final Integer RESET_PHONE_CODE_COUNT_LIMIT = 5;

    static final String WECHAT_BIND_CODE_COUNT = "WECHAT_BIND_CODE_COUNT:";

    static final Integer WECHAT_BIND_CODE_COUNT_LIMIT = 5;

    static final String FORGET_PASSWORD_CODE_COUNT = "WECHAT_BIND_CODE_COUNT:";

    static final Integer FORGET_PASSWORD_CODE_COUNT_LIMIT = 5;

    static final Integer SESSION_VALID_DAYS = 31;

    static final Integer PUBLISH_VALID_DAYS = 30;

    static final String BANK_CARDS = "BANK_CARDS";

    static final String WECHAT_MINI_USER_INFO = "WECHAT_MINI_USER_INFO:";

    static final String WECHAT_APP_USER_INFO = "WECHAT_APP_USER_INFO:";

    static final Integer UUID_VALID_TIME = 3600 * 24 * 31;
    /**服务器文件存储路径**/
    static final String SERVER_FILE_PATH="/home/qujiali/appstore/";
    /** 日志表状态 */
    interface JOBSTATE {
        /**
         * 日志表状态，初始状态，插入
         */
        static final String INIT_STATS = "I";
        /**
         * 日志表状态，成功
         */
        static final String SUCCESS_STATS = "S";
        /**
         * 日志表状态，失败
         */
        static final String ERROR_STATS = "E";
        /**
         * 日志表状态，未执行
         */
        static final String UN_STATS = "N";
    }

    /** 短信验证码类型 */
    public interface MSGCHKTYPE {
        /** 注册 */
        public static final String REGISTER = CACHE_NAMESPACE + "REGISTER:";
        /** 登录 */
        public static final String LOGIN = CACHE_NAMESPACE + "LOGIN:";
        /** 修改密码验证码 */
        public static final String CHGPWD = CACHE_NAMESPACE + "CHGPWD:";
        /** 身份验证验证码 */
        public static final String VLDID = CACHE_NAMESPACE + "VLDID:";
        /** 信息变更验证码 */
        public static final String CHGINFO = CACHE_NAMESPACE + "CHGINFO:";
        /** 活动确认验证码 */
        public static final String AVTCMF = CACHE_NAMESPACE + "AVTCMF:";
    }
    /** 微信小程序常量 **/
    

    /** 小程序微信服务端url **/
    static final String WECHATURLHEAD="https://api.weixin.qq.com/sns/jscode2session?appid=wxc913b31c1ff004a4&secret=cb358689d2d72bd55228f9ebb5fc5fc9&js_code=";
    static final String WECHATURLEDN="&grant_type=authorization_code";
    
    public static Integer GET_CASH_LIMIT = 2000;

    static final String SMS_FAST_LOGIN_CODE = "sms:fast:login:code:";

    static final String FAST_LOGIN_CODE_COUNT = "FAST_LOGIN_CODE_COUNT:";

    static final Integer FAST_LOGIN_CODE_COUNT_LIMIT = 5;
    
      /** 固定地图查询常量 **/
    static final Integer DISTANCE=2000;

    static final String VALIDATE_TIME = ":validate:time:";
    /*用来校验错误*/
    public static final String ERROE = "error";
    /**订单状态**/
    //下单
    static final Integer SUBMITORDER=1;
    //后台管理确认订单
    static final Integer COMFIRMORDER=2;
    //拒绝下单
    static final Integer REFUSEORDER=-1;

}
