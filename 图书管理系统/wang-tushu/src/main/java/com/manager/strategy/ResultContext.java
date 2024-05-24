package com.manager.strategy;

import com.manager.common.common.AjaxResult;


public class ResultContext<T> {
    private ResultStrategy<T> strategy;

    public void setStrategy(ResultStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public AjaxResult executeStrategy(String message, Object data) {
        return strategy.getResult(message,data);
    }
}
