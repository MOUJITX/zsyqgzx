package com.moujitx.zjweuProbity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String name;
    private String type;
    private String college;
    private String classname;
    private String grade;
    private String phone;
    private String qq;
    private String campus;
    private String male;
    private String person;
    private String nation;
    private String birth;
    private String politic;
    private String province;
    private String graduate;
    private String address;
    private String admission;
    private String major;
    private String level;
    private String status;
    private String detail;
}
