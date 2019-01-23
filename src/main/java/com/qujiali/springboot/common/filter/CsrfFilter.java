package com.qujiali.springboot.common.filter;

import com.qujiali.springboot.common.utils.FileUtil;
import com.qujiali.springboot.common.utils.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CsrfFilter implements Filter {
	private Logger logger = LogManager.getLogger();

	// 白名单
	private List<String> whiteUrls;

	private int _size = 0;
	@Override
	public void init(FilterConfig filterConfig) {
		// 读取文件
		String path = CsrfFilter.class.getResource("/").getFile();
		whiteUrls = FileUtil.readFile(path + "white/csrfWhite.txt");
		_size = null == whiteUrls ? 0 : whiteUrls.size();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			// 获取请求url地址
			String url = req.getRequestURL().toString();
			String referurl = req.getHeader("Referer");
			if (isWhiteReq(referurl)) {
				chain.doFilter(request, response);
			} else {
				req.getRequestDispatcher("/").forward(req, res);

				// 记录跨站请求日志
				String log = "";
				String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				String clientIp = WebUtil.getHost(req);

				log = "跨站请求---->>>" + clientIp + "||" + date + "||" + referurl + "||" + url;
				logger.warn(log);

				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();

				out.println("{\"code\":\"308\",\"msg\":\"错误的请求头信息\"}");
				out.flush();
				out.close();
				return;
			}

		} catch (Exception e) {
			logger.error("doFilter", e);
		}

	}

	/*
	 * 判断是否是白名单
	 */
	private boolean isWhiteReq(String referUrl) {
		if (referUrl == null || "".equals(referUrl) || _size == 0) {
			return true;
		} else {
			String refHost = "";
			referUrl = referUrl.toLowerCase();
			if (referUrl.startsWith("http://")) {
				refHost = referUrl.substring(7);
			} else if (referUrl.startsWith("https://")) {
				refHost = referUrl.substring(8);
			}

			for (String urlTemp : whiteUrls) {
				if (refHost.indexOf(urlTemp.toLowerCase()) > -1) {
					return true;
				}
			}
		}

		return false;
	}
	@Override
	public void destroy() {

	}
}
