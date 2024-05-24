package com.manager.strategy;

import com.manager.common.common.AjaxResult;


public interface ResultStrategy<T> {
    AjaxResult getResult(String message, Object data);
}
