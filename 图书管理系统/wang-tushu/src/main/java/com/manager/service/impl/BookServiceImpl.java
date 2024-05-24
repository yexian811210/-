package com.manager.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.manager.common.annotation.CustomTransaction;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Book;
import com.manager.mapper.BookMapper;
import com.manager.service.BookService;
import com.manager.utils.AliOssUtil;
import com.manager.utils.AssertUtils;
import com.manager.utils.PageUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private AliOssUtil aliOssUtil;

    @Resource
    private BookMapper bookMapper;



    @Override
    public PageResult<Book> pageSearch(PageRequest pageRequest) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        String key = pageRequest.getKey();
        if (AssertUtils.isNotEmpty(key)) {
            wrapper.like(Book::getBookName, key);
        }
        wrapper.eq(Book::getDelFlag,0);
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Book> bookList = this.baseMapper.selectList(wrapper);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);
        return PageUtils.getPageResult(pageInfo);
    }


    @CustomTransaction
    @Override
    public AjaxResult<Object> deleteBook(Long id) {
        Book book = this.getById(id);
        this.baseMapper.deleteById(id);
        if(book.getImage()!=null){
            aliOssUtil.delete(book.getImage().replaceFirst("^.*//.*aliyuncs.com/", "").replaceFirst("\\?.*",""));
        }
        //删除阿里云oss存储图片
        return AjaxResult.success();
    }

    @SneakyThrows
    @Override
    public AjaxResult<Object> updateBook(MultipartHttpServletRequest request) {
        Book book = JSON.parseObject(request.getParameter("book"), Book.class);
        MultipartFile file = request.getFile("file");
        if (Objects.nonNull(file)){
            aliOssUtil.delete(book.getImage().replaceFirst("^.*//.*aliyuncs.com/", "").replaceFirst("\\?.*",""));
            String url = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
            book.setImage(url);
        }
        book.setUpdateTime(new Date());
        this.baseMapper.updateById(book);
        return AjaxResult.success();
    }


    /**
     * 新增用户
     */
    @SneakyThrows
    @Override
    @CustomTransaction("图书新增失败")
    public AjaxResult<Object> addBook(MultipartHttpServletRequest request, HttpServletRequest IpRequest) {
        Book book = JSON.parseObject(request.getParameter("book"), Book.class);
        MultipartFile file = request.getFile("file");
        if(Objects.nonNull(file)){
            String url =  aliOssUtil.upload(Objects.requireNonNull(file).getBytes(), file.getOriginalFilename());
            book.setImage(url);
        }
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());
        book.setDelFlag(0);
        this.baseMapper.insertReturnBookId(book);
        return AjaxResult.success();
    }
}
