package com.moujitx.zjweuProbity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moujitx.zjweuProbity.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    @Select("SELECT * from department")
    List<Department> findAll();
}
