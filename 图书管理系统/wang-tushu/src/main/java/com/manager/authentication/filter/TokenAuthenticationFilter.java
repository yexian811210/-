package com.manager.authentication.filter;

import com.manager.authentication.LoginUserDetails;
import com.manager.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private String loginUri;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (token == null || "".equals(token) || Objects.equals(request.getRequestURI(), loginUri)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId = JwtUtils.parseToken(token);
        LoginUserDetails loginUserDetails = (LoginUserDetails) redisTemplate.opsForValue().get("user:" + userId);
        if (Objects.isNull(loginUserDetails)) {
            throw new RuntimeException("用户未登录");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDetails, null, loginUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    @Autowired
    public void setLoginUri(Environment environment) {
        String loginUri = environment.getProperty("application.login.uri");
        String contextPath = environment.getProperty("server.servlet.context-path");
        this.loginUri = contextPath == null ? loginUri : contextPath + loginUri;
    }
}
