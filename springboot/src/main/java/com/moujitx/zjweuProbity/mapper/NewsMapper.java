package com.moujitx.zjweuProbity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moujitx.zjweuProbity.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    @Select("SELECT * from news")
    List<News> findAll();
}
