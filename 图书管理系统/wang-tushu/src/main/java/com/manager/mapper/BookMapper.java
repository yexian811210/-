package com.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manager.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书Mapper
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    int insertReturnBookId(Book book);
}




