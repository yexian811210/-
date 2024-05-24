package com.manager.controller;

import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Book;
import com.manager.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;


    @GetMapping("/pageSearch")
    public AjaxResult<PageResult<Book>> pageSearch(PageRequest pageRequest) {
        PageResult<Book> pageResult = bookService.pageSearch(pageRequest);
        return AjaxResult.success(pageResult);
    }


    @GetMapping("/deleteBook")
    public AjaxResult<Object> deleteBook(@RequestParam("id") Long id) {
        return bookService.deleteBook(id);
    }

    @PostMapping("/updateBook")
    public AjaxResult<Object> updateBook(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return bookService.updateBook(multipartRequest);
    }

    @PostMapping("/addBook")
    public AjaxResult<Object> addBook(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return bookService.addBook(multipartRequest,request);
    }
}
