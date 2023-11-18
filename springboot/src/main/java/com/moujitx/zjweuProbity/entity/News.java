package com.moujitx.zjweuProbity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("news")
@Data
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String top;
    private String publish;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    private String source;
    private String author;
    private String detail;
    private String simple;
    private String adduser;
    private String edituser;

}
