package com.manager.common.common;

import java.util.List;

public class PageResult<T> {
    /**
     * 总记录数
     */
    private Long total = 0L;
    /**
     * 总页数
     */
    private Integer totalPage = 0;
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 当前页记录条数
     */
    private Integer size = 0;
    /**
     * 数据列表
     */
    private List<T> dataList;

    public PageResult() {
    }

    public PageResult(Long total, Integer totalPage, Integer pageNum, Integer pageSize, Integer size, List<T> dataList) {
        this.total = total;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.size = size;
        this.dataList = dataList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getpageNum() {
        return pageNum;
    }

    public void setpageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", dataList=" + dataList +
                '}';
    }
}
