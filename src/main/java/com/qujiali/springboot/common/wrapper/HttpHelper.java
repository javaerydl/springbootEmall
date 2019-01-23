package com.qujiali.springboot.common.wrapper;
import javax.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HttpHelper {
	private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        	logger.error("{}",e);
        } finally {
        		try {
        			if (reader != null) {
        				reader.close();
        			}
        			if (inputStream != null) {
        				 inputStream.close();
        			}
        		} catch (IOException e) {
        			logger.error("{}",e);
        		}
        }
        return sb.toString();
    }
}
