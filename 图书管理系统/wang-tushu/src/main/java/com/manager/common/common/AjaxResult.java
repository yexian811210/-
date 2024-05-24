package com.manager.common.common;

import java.io.Serializable;

public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = 3769545126082051926L;

    private Integer code;
    private String message;
    private T data;

    public AjaxResult() {
    }

    public AjaxResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<T>(200, "success", null);
    }

    public static <T> AjaxResult<T> success(String message) {
        return new AjaxResult<T>(200, message, null);
    }


    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<T>(200, "success", data);
    }

    public static <T> AjaxResult<T> success(String message, T data) {
        return new AjaxResult<T>(200, message, data);
    }

    public static <T> AjaxResult<T> success(Integer code, String message, T data) {
        return new AjaxResult<>(code, message, data);
    }

    public static <T> AjaxResult<T> fail() {
        return new AjaxResult<T>(400, "fail", null);
    }

    public static <T> AjaxResult<T> fail(String message) {
        return new AjaxResult<T>(400, message, null);
    }

    public static <T> AjaxResult<T> fail(T data) {
        return new AjaxResult<T>(400, "fail", data);
    }


    public static <T> AjaxResult<T> fail(String message, T data) {
        return new AjaxResult<T>(400, message, data);
    }

    public static <T> AjaxResult<T> fail(Integer code, String message, T data) {
        return new AjaxResult<T>(code, message, data);
    }


    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
