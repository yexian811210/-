package com.manager.common.aop;

import com.manager.common.annotation.LogToMysql;
import com.manager.utils.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class LogToMysqlAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogToMysqlAspect.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(logToMysql)")
    public void logToMysqlOperation(LogToMysql logToMysql) {
    }

    @AfterReturning(value = "logToMysqlOperation(logToMysql)", returning = "result", argNames = "joinPoint, logToMysql, result")
    public void logToMysql(JoinPoint joinPoint, LogToMysql logToMysql, Object result) {
        try {
            //方法名
            String methodName = joinPoint.getSignature().getName();
            String logMessage = logToMysql.value();

            // 获取其他需要记录的信息，如参数、返回值等
            Object[] args = joinPoint.getArgs();
            Object returnValue = result;

            // 将日志信息插入到数据库中
            String sql = "INSERT INTO sys_log (ip,method,time) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, IpUtil.getIpAddress(request),logMessage ,new Date());
            logger.info("Log recorded to MySQL");
        } catch (Exception e) {
            logger.error("Error recording log to MySQL: " + e.getMessage());
        }
    }
}
