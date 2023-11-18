package com.moujitx.zjweuProbity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moujitx.zjweuProbity.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * from user")
    List<User> findAll();

    List<User> selectList(String name, String role, String username);
}
