package com.manager.handler;

import com.manager.common.common.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * url请求参数不符合校验规则
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxResult<Map<String, String>> handleConstraintViolationException(ConstraintViolationException exception) {
        printException(exception);
        Map<String, String> errorMap = new HashMap<>();
        Set<ConstraintViolation<?>> constraintSet = exception.getConstraintViolations();
        constraintSet.forEach(constraint -> {
            String propertyPath = constraint.getPropertyPath().toString();
            errorMap.put(propertyPath.substring(propertyPath.indexOf(".") + 1), constraint.getMessage());
        });
        return AjaxResult.fail(errorMap);
    }

    /**
     * url对象请求参数不符合校验规则
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult<Map<String, String>> handleBindException(BindException exception) {
        printException(exception);
        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> errorList = exception.getFieldErrors();
        errorList.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return AjaxResult.fail(errorMap);
    }

    /**
     * url请求参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        printException(exception);
        return AjaxResult.fail("请求参数异常：参数" + exception.getParameterName() + "缺失");
    }

    /**
     * Json请求参数不符合校验规则
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    public ResponseResult<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
//        printException(exception);
//        Map<String, String> errorMap = new HashMap<>();
//        List<FieldError> errorList = exception.getFieldErrors();
//        errorList.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
//        return ResponseResult.fail(errorMap);
//    }

    /**
     * 请求体缺失
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public AjaxResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        printException(exception);
        return AjaxResult.fail("http消息读取异常：" + exception.getMessage());
    }

    /**
     * 路径变量缺失
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({MissingPathVariableException.class})
    public AjaxResult<String> handleMissingPathVariableException(MissingPathVariableException exception) {
        printException(exception);
        return AjaxResult.fail("路径变量异常：" + exception.getMessage());
    }


    /**
     * 请求方法错误
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public AjaxResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        printException(exception);
        return AjaxResult.fail("http请求方法异常：" + exception.getMessage());
    }

    /**
     * http媒体类型异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public AjaxResult<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        printException(exception);
        return AjaxResult.fail("http媒体类型异常:" + exception.getMessage());
    }

    /**
     * 文件获取异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({MissingServletRequestPartException.class})
    public AjaxResult<String> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        printException(exception);
        return AjaxResult.fail("文件获取异常:" + exception.getMessage());
    }

    /**
     * Servlet请求绑定异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({ServletRequestBindingException.class})
    public AjaxResult<String> handleServletRequestBindingException(ServletRequestBindingException exception) {
        printException(exception);
        return AjaxResult.fail("Servlet请求绑定异常：" + exception.getMessage());
    }

    /**
     * 接口超时异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({AsyncRequestTimeoutException.class})
    public AjaxResult<String> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException exception) {
        printException(exception);
        return AjaxResult.fail("接口超时异常：" + exception.getMessage());
    }


    /**
     * http消息不可写异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({HttpMessageNotWritableException.class})
    public AjaxResult<String> handleHttpMessageNotWritableException(HttpMessageNotWritableException exception) {
        printException(exception);
        return AjaxResult.fail("http消息不可写异常：" + exception.getMessage());
    }


    /**
     * sql异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({BadSqlGrammarException.class})
    public AjaxResult<String> handleBadSqlGrammarException(BadSqlGrammarException exception) {
        printException(exception);
        String sqlException = exception.getSQLException().toString();
        String message = sqlException.substring(sqlException.indexOf(":") + 1);
        return AjaxResult.fail("SQL异常：" + message.trim());
    }


    @ExceptionHandler(NullPointerException.class)
    public AjaxResult<String> handleNullException(NullPointerException exception) {
        printException(exception);
        return AjaxResult.fail("空指针异常");
    }


    @ExceptionHandler(IndexOutOfBoundsException.class)
    public AjaxResult<String> handleIndexOutOfBoundsException(IndexOutOfBoundsException exception) {
        printException(exception);
        return AjaxResult.fail("数组下标越界异常");
    }

    @ExceptionHandler(ClassCastException.class)
    public AjaxResult<String> handleClassCastException(ClassCastException exception) {
        printException(exception);
        return AjaxResult.fail("类转换异常：" + exception.getMessage());
    }


    @ExceptionHandler(NumberFormatException.class)
    public AjaxResult<String> handleClassCastException(NumberFormatException exception) {
        printException(exception);
        return AjaxResult.fail("数字格式异常：" + exception.getMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult<String> handleAccessDeniedException(AccessDeniedException exception) {
        printException(exception);
        return AjaxResult.fail(403,"无访问权限",null);
    }



    @ExceptionHandler(Exception.class)
    public AjaxResult<String> handleException(Exception exception) {
        printException(exception);
        return AjaxResult.fail("系统异常");
    }


    private void printException(Exception exception) {
        log.error("");
        log.error("--------------------异常信息---------------------------");
        log.error("异常时间: " + formatter.format(LocalDateTime.now()));
        log.error("异常类型: " + exception.getClass().getName());
        log.error("异常信息: " + exception.getMessage());
        log.error("-----------------------------------------------------");
        log.error(getTrace(exception));

    }

    /**
     * 获取异常信息
     *
     * @param throwable
     * @return
     */
    public String getTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        StringBuffer stringBuffer = stringWriter.getBuffer();
        return stringBuffer.toString();
    }


}
