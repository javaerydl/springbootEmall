package com.qujiali.springboot.common.utils;

import com.alibaba.fastjson.JSON;
import com.qujiali.springboot.common.support.Token;


public class TokenUtil {
	public static void setTokenInfo(String token, String value) {
		try {
			Token tokenInfo = new Token();
			tokenInfo.setTime(System.currentTimeMillis());
			tokenInfo.setValue(value);
			CacheUtil.getLockManager().hset(Constants.TOKEN_KEY, token, JSON.toJSONString(tokenInfo));
		} catch (Exception e) {
			throw new RuntimeException("保存token失败，错误信息：", e);
		}
	}

	public static void delToken(String token) {
		try {
			CacheUtil.getLockManager().hdel(Constants.TOKEN_KEY, token);
		} catch (Exception e) {
			throw new RuntimeException("删除token失败，错误信息：", e);
		}
	}

	public static Token getTokenInfo(String token) {
		String value = (String) CacheUtil.getLockManager().hget(Constants.TOKEN_KEY, token);
		Token tokenInfo = JSON.parseObject(value, Token.class);
		return tokenInfo;
	}
}
