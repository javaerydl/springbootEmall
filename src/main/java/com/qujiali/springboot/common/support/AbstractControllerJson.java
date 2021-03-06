/**
 * 
 */
package com.qujiali.springboot.common.support;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qujiali.springboot.common.exception.BaseException;
import com.qujiali.springboot.common.exception.HttpCode;
import com.qujiali.springboot.common.exception.IllegalParameterException;
import com.qujiali.springboot.common.utils.Constants;
import com.qujiali.springboot.common.utils.InstanceUtil;
import com.qujiali.springboot.common.utils.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.plugins.Page;



/**
 * 控制器基类
 */
public abstract class AbstractControllerJson {
	protected Logger logger = LogManager.getLogger();

	/** 获取当前用户Id */
	protected Long getCurrUser() {
		return WebUtil.getCurrentUser();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new DateFormat(), true));
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<String> setSuccessModelMap() {
		return setSuccessModelMap(new ModelMap(), null);
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<String> setSuccessModelMap(ModelMap modelMap) {
		return setSuccessModelMap(modelMap, null);
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<String> setSuccessModelMap(Object data) {
		return setModelMap(new ModelMap(), HttpCode.OK, data);
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<String> setSuccessModelMap(ModelMap modelMap, Object data) {
		return setModelMap(modelMap, HttpCode.OK, data);
	}

	/** 设置响应代码 */
	protected ResponseEntity<String> setModelMap(HttpCode code) {
		return setModelMap(new ModelMap(), code, null);
	}

	/** 设置响应代码 */
	protected ResponseEntity<String> setModelMap(ModelMap modelMap, HttpCode code) {
		return setModelMap(modelMap, code, null);
	}

	/** 设置响应代码 */
	protected ResponseEntity<String> setModelMap(HttpCode code, Object data) {
		return setModelMap(new ModelMap(), code, data);
	}

	/** 设置响应代码 */
	protected ResponseEntity<String> setModelMap(ModelMap modelMap, HttpCode code, Object data) {
		if (!modelMap.isEmpty()) {
			Map<String, Object> map = InstanceUtil.newLinkedHashMap();
			map.putAll(modelMap);
			modelMap.clear();
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				if (!key.startsWith("org.springframework.validation.BindingResult") && !key.equals("void")) {
					modelMap.put(key, map.get(key));
				}
			}
		}
		if (data != null) {
			if (data instanceof Page) {
				Page<?> page = (Page<?>) data;
				modelMap.put("rows", page.getRecords());
				modelMap.put("current", page.getCurrent());
				modelMap.put("size", page.getSize());
				modelMap.put("pages", page.getPages());
				modelMap.put("total", page.getTotal());
			} else if (data instanceof List<?>) {
				modelMap.put("rows", data);
				modelMap.put("total", ((List<?>) data).size());
			} else {
				modelMap.put("data", data);
			}
		}
		modelMap.put("code", code.value().toString());
		modelMap.put("msg", code.msg());
		modelMap.put("timestamp", System.currentTimeMillis());
		logger.info("RESPONSE : " + JSON.toJSONString(modelMap));
		return ResponseEntity.ok(JSON.toJSONString(modelMap, SerializerFeature.WriteNonStringValueAsString));
	}

	/** 异常处理 */
	@ExceptionHandler(Exception.class)
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws Exception {
		logger.error(Constants.Exception_Head, ex);
		ModelMap modelMap = new ModelMap();
		if (ex instanceof BaseException) {
			((BaseException) ex).handler(modelMap);
		} else if (ex instanceof IllegalArgumentException) {
			new IllegalParameterException(ex.getMessage()).handler(modelMap);
		} else if (ex instanceof UnauthorizedException) {
			modelMap.put("code", HttpCode.FORBIDDEN.value().toString());
			modelMap.put("msg", HttpCode.FORBIDDEN.msg());
		} else {
			modelMap.put("code", HttpCode.INTERNAL_SERVER_ERROR.value().toString());
			String msg = StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.INTERNAL_SERVER_ERROR.msg());
			logger.debug(msg);
			modelMap.put("msg", msg.length() > 100 ? "系统走神了,请稍候再试." : msg);
		}
		response.setContentType("application/json;charset=UTF-8");
		modelMap.put("timestamp", System.currentTimeMillis());
		logger.info("RESPONSE : " + JSON.toJSON(modelMap));
		byte[] bytes = JSON.toJSONBytes(modelMap, SerializerFeature.DisableCircularReferenceDetect);
		response.getOutputStream().write(bytes);
	}
}
