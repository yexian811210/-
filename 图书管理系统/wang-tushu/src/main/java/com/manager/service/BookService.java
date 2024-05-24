package com.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Book;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public interface BookService extends IService<Book> {

    PageResult<Book> pageSearch(PageRequest pageRequest);

    AjaxResult<Object> deleteBook(Long id);

    AjaxResult<Object> updateBook(MultipartHttpServletRequest request);

    AjaxResult<Object> addBook(MultipartHttpServletRequest request, HttpServletRequest IpRequest);

}
