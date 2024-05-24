package com.manager.strategy;

import com.manager.common.common.AjaxResult;
import org.apache.poi.ss.formula.functions.T;


public class FailResultStrategy implements ResultStrategy<T> {
    @Override
    public AjaxResult getResult(String message, Object data) {
        return AjaxResult.fail(message);
    }
}
