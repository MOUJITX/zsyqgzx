package com.moujitx.zjweuProbity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("term")
@Data
public class Term {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String pcmc;
    private String pcdm;
    private String dqzt;
    private String zssm;
    private String xmbz;
}