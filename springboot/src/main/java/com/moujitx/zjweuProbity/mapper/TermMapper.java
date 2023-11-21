package com.moujitx.zjweuProbity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moujitx.zjweuProbity.entity.Term;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TermMapper extends BaseMapper<Term> {
    @Select("SELECT * from term")
    List<Term> findAll();
}
