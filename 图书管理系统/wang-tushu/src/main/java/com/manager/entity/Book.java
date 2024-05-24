package com.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value ="book")
@Data
public class Book implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String bookName;

    /**
     * 头像
     */
    private String image;



    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;
    /**
     * 简介
     */
    private String content;
    /**
     * 作者
     */
    private String author;


}
