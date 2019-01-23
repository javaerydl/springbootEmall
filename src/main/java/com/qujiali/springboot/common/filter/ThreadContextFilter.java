package com.qujiali.springboot.common.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;

public class ThreadContextFilter implements Filter {
	  @Override
	    public void init(FilterConfig filterConfig) throws ServletException {

	    }

	    @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	        try {
	            ThreadContext.put("UUID", UUID.randomUUID().toString().replaceAll("-", ""));    //StaticUUID是自己写的类，用于生成UUID常量。
	            //ThreadContext.put("ip", request.getLocalAddr());
	            chain.doFilter(request, response);
	        } finally {
	        	//清除ThreadContext,避免内存泄露
	            ThreadContext.clearAll();
	        }
	    }

	    @Override
	    public void destroy() {

	    }

}
