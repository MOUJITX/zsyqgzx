package com.moujitx.zjweuProbity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("department")
@Data
public class Department {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String number;
    private String name;
    private String detail;
    private String qtlxr;
    private String qtdh;
    private String qtbgdz;
    private String qtrs;
    private String nxlxr;
    private String nxdh;
    private String nxbgdz;
    private String nxrs;
    private String gwsm;
    private String pytj;
}