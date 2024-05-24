package com.manager.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@TableName(value ="sys_log")
@Data
@AllArgsConstructor
public class Log implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *Ip
     */
    private String ip;

    private String method;

    private String time;


}
