package com.manager.authentication.handler;

import com.alibaba.fastjson.JSON;
import com.manager.common.common.AjaxResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无访问权限处理器
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AjaxResult<String> responseResult = AjaxResult.fail(403, "无访问权限", null);
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
