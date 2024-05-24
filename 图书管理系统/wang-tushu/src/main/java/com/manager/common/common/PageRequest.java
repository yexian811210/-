package com.manager.common.common;

public class PageRequest {

    private Integer pageSize = 10;

    private Integer pageNum = 1;

    private String key;

    public PageRequest() {
    }

    public PageRequest(Integer pageSize, Integer pageNum, String key) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.key = key;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", key='" + key + '\'' +
                '}';
    }
}
