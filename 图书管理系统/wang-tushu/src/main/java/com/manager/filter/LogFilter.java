package com.manager.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class LogFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(LogFilter.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //需打印请求头名称
    private final List<String> headerNames = Collections.singletonList("token");


    private static ApplicationContext applicationContext;

    @Resource
    public void setApplicationContext(ApplicationContext applicationContext) {
        LogFilter.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isRequestValid(request)) {
            return;
        }
        request = new ContentCachingRequestWrapper(request);
        response = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long endtTime = System.currentTimeMillis();
        if (isNotExcludeUri(request.getRequestURI()) && !Objects.equals(response.getStatus(), 404)) {
            printLog(request, response, endtTime - startTime);
        }
        //返回响应体
        returnResponse(response);

    }


    /**
     * 打印日志
     */
    private synchronized void printLog(HttpServletRequest request, HttpServletResponse response, long totalTime) {
        log.info("--------------------请求开始---------------------------");
        log.info("请求时间: " + formatter.format(LocalDateTime.now()));
        log.info("请求IP : " + ("0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? "127.0.0.1" : request.getRemoteAddr()));
        log.info("请求方式: " + request.getMethod());
        log.info("请求路径: " + request.getRequestURL().toString());
        log.info("请求耗时: " + totalTime + "ms");
        log.info("请求头 : " + getHeaders(request));
        log.info("请求参数: " + getParameters(request));
        log.info("请求体 : " + getRequestBody(request));
        log.info("响应状态: " + response.getStatus());
        log.info("响应数据: " + getResponseBody(response));
        log.info("--------------------请求结束---------------------------");

    }


    /**
     * 判断请求是否合法
     *
     * @param request:
     * @return :
     */
    private boolean isRequestValid(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String urlRegex = "^(((ht|f)tps?)://)?([\\w-]+(\\.[\\w-]+)+|localhost)([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$";
        return url.matches(urlRegex);
    }


    /**
     * 获取请求Body
     *
     * @param request:
     * @return :
     */
    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return requestBody;
    }

    /**
     * 获取响应Body
     *
     * @param response:
     * @return :
     */
    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    /**
     * 返回响应
     *
     * @param response:
     * @throws IOException:
     */
    private void returnResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (responseWrapper != null) {
            responseWrapper.copyBodyToResponse();
        }
    }


    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    private Map<String, String> getParameters(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        return params;
    }


    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        headerNames.forEach(headerName -> headers.put(headerName, request.getHeader(headerName)));
        return headers;
    }


    /**
     * 判断是否为静态资源请求
     *
     * @param uri
     * @return
     */
    private boolean isNotStaticFile(String uri) {
        return !uri.matches("^.*/.*\\..+$");
    }

    /**
     * 判断是否是swagger请求
     */
    private boolean isNotSwaggerUrl(String uri) {
        return !(uri.matches("^.*/swagger-resources.*$")
                || uri.matches("^.*/webjars/.*$")
                || uri.matches("^.*/v2/api-docs.*$")
                || uri.matches("^.*/swagger-ui\\.html$")
                || uri.matches("^.*/doc\\.html$"));
    }

    /**
     * 是否是排除请求
     *
     * @param uri
     * @return
     */
    private boolean isNotExcludeUri(String uri) {
        return isNotSwaggerUrl(uri) && isNotStaticFile(uri);
    }

}
