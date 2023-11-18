package com.moujitx.zjweuProbity.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moujitx.zjweuProbity.common.Result;
import com.moujitx.zjweuProbity.entity.Term;
import com.moujitx.zjweuProbity.mapper.TermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/term")
public class TermController {
    //@Resource
    //TermMapper termMapper;

    @Autowired
    private TermMapper termMapper;

    //查找全部
    @GetMapping("/")
    public List<Term> index() {
        List<Term> all = termMapper.findAll();
        return all;
    }

    //新增
    @PostMapping
    public Result<?> save(@RequestBody Term term) {
        Term res = termMapper.selectOne(Wrappers.<Term>lambdaQuery().eq(Term::getPcdm, term.getPcmc()));
        if (res != null) {
            return Result.error("-1", "当前学年已存在！");
        }
        termMapper.insert(term);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Term term) {
        termMapper.updateById(term);
        return Result.success();
    }


    //删除
    @DeleteMapping("/del/{id}")
    public Result<?> update(@PathVariable Long id) {
        termMapper.deleteById(id);
        return Result.success();
    }


    //查找关键词
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Term> wrapper = Wrappers.<Term>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Term::getPcmc, search);
        }
        Page<Term> termPage = termMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(termPage);
    }
}
