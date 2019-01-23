package com.qujiali.springboot.common.filter;

import com.qujiali.springboot.common.wrapper.BodyReaderHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HttpServletRequestReplacedFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(HttpServletRequestReplacedFilter.class);

    /* 这里设置不被拦截的请求路径 */
    private static final List<String> unFilterUrlList = Arrays.asList("/app/image");

    /* 判断请求路径是否为不拦截的请求路径 */
    private boolean isFilter(String url) {
        for (String s : unFilterUrlList) {
            if (url.indexOf(s) > -1) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (isFilter(httpServletRequest.getRequestURL().toString())) {
                logger.info("放过了:" + httpServletRequest.getRequestURL().toString());
            } else {
                if ("POST".equals(httpServletRequest.getMethod().toUpperCase())) {
                    requestWrapper = new BodyReaderHttpServletRequestWrapper(
                            (HttpServletRequest) request);
                }
                if ("PUT".equals(httpServletRequest.getMethod().toUpperCase())) {
                    requestWrapper = new BodyReaderHttpServletRequestWrapper(
                            (HttpServletRequest) request);
                }
            }
        }

        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
