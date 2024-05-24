package com.manager.utils;

import com.github.pagehelper.PageInfo;
import com.manager.common.common.PageResult;


public class PageUtils {

    /**
     * 封装 pagehelper 分页结果
     */
    public static <T> PageResult<T> getPageResult(PageInfo<T> pageInfo) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setpageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setDataList(pageInfo.getList());
        pageResult.setSize(pageInfo.getSize());
        return pageResult;
    }


}
