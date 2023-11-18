package com.moujitx.zjweuProbity.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moujitx.zjweuProbity.common.Result;
import com.moujitx.zjweuProbity.entity.News;
import com.moujitx.zjweuProbity.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    //@Resource
    //NewsMapper newsMapper;

    @Autowired
    private NewsMapper newsMapper;

    //查找全部
    @GetMapping("/")
    public List<News> index() {
        List<News> all = newsMapper.findAll();
        return all;
    }

    //新增
    @PostMapping
    public Result<?> save(@RequestBody News news) {
        newsMapper.insert(news);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody News news) {
        newsMapper.updateById(news);
        return Result.success();
    }


    //删除
    @DeleteMapping("/del/{id}")
    public Result<?> update(@PathVariable Long id) {
        newsMapper.deleteById(id);
        return Result.success();
    }


    //查找关键词
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(News::getTitle, search);
        }
        Page<News> newsPage = newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(newsPage);
    }
}
