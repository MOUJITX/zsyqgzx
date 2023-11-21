package com.moujitx.zjweuProbity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moujitx.zjweuProbity.entity.Qgsq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QgsqMapper extends BaseMapper<Qgsq> {
    @Select("SELECT * from qgsq")
    List<Qgsq> findAll();
}
