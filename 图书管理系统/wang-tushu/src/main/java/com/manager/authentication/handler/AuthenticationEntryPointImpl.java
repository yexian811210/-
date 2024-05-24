package com.manager.authentication.handler;

import com.alibaba.fastjson.JSON;
import com.manager.common.common.AjaxResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AjaxResult<String> responseResult = AjaxResult.fail(401, "用户认证失败", null);
        setResponseBody(response, JSON.toJSONString(responseResult));
    }


    private void setResponseBody(HttpServletResponse response, String message) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
