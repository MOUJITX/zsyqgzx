package com.moujitx.zjweuProbity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("qgsq")
@Data
public class Qgsq {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String sqxn;
    private String speed;
    private String name;
    private String username;
    private String male;
    private String college;
    private String classname;
    private String phone;
    private String qq;
    private String type;
    private String skill;
    private String depart;
    private String departb;
    private String choosetj;
    private String lastdepart;
    private Integer timea;
    private Integer timeb;
    private Integer timec;
    private Integer timed;
    private Integer timee;
    private Integer timef;
    private Integer timeg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uptime;
    private String reply;
    private String replychoose;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replytime;
    private String replyname;
    private String replynum;
}