package com.moujitx.zjweuProbity.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moujitx.zjweuProbity.common.Result;
import com.moujitx.zjweuProbity.entity.Qgsq;
import com.moujitx.zjweuProbity.mapper.QgsqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/qgsq")
public class QgsqController {
    //@Resource
    //QgsqMapper qgsqMapper;

    @Autowired
    private QgsqMapper qgsqMapper;

    //导出http://124.221.218.223:88/api/qgsq/export?name=&username=&college=%E4%BF%A1%E6%81%AF%E5%B7%A5%E7%A8%8B%E5%AD%A6%E9%99%A2&term=2022-2023&type=&speed=
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") String username,
                       @RequestParam(defaultValue = "") String college,
                       @RequestParam(defaultValue = "") String term,
                       @RequestParam(defaultValue = "") String type,
                       @RequestParam(defaultValue = "") String speed
    ) throws Exception {
        LambdaQueryWrapper<Qgsq> queryWrapper = Wrappers.<Qgsq>lambdaQuery();
        queryWrapper.like(Qgsq::getName, name);
        queryWrapper.like(Qgsq::getUsername, username);
        queryWrapper.like(Qgsq::getCollege, college);
        queryWrapper.like(Qgsq::getSqxn, term);
        queryWrapper.like(Qgsq::getType, type);
        queryWrapper.like(Qgsq::getSpeed, speed);

        List<Qgsq> list = qgsqMapper.selectList(queryWrapper);         // 从数据库查询出所有的数据
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");    // 通过工具类创建writer 写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(true);        // 在内存操作，写出到浏览器
        //自定义标题别名
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("speed", "当前进度");
        writer.addHeaderAlias("sqxn", "申请学年");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("username", "学号");
        writer.addHeaderAlias("male", "性别");
        writer.addHeaderAlias("college", "学院");
        writer.addHeaderAlias("classname", "班级");
        writer.addHeaderAlias("phone", "联系电话");
        writer.addHeaderAlias("qq", "QQ号");
        writer.addHeaderAlias("type", "资助认定");
        writer.addHeaderAlias("skill", "个人技能");
        writer.addHeaderAlias("depart", "申请岗位1");
        writer.addHeaderAlias("departb", "申请岗位2");
        writer.addHeaderAlias("choosetj", "是否调剂");
        writer.addHeaderAlias("lastdepart", "上学年勤工经历");
        writer.addHeaderAlias("timea", "周一空闲");
        writer.addHeaderAlias("timeb", "周二空闲");
        writer.addHeaderAlias("timec", "周三空闲");
        writer.addHeaderAlias("timed", "周四空闲");
        writer.addHeaderAlias("timee", "周五空闲");
        writer.addHeaderAlias("timef", "周六空闲");
        writer.addHeaderAlias("timeg", "周日空闲");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("勤工助学申请信息导出", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    //查询全部
    @GetMapping("/")
    public List<Qgsq> index() {
        List<Qgsq> all = qgsqMapper.findAll();
        return all;
    }


    //新增
    @PostMapping
    public Result<?> save(@RequestBody Qgsq qgsq) {
        Qgsq res = qgsqMapper.selectOne(Wrappers.<Qgsq>lambdaQuery()
                .eq(Qgsq::getUsername, qgsq.getUsername())
                .eq(Qgsq::getSqxn, qgsq.getSqxn()));
        if (res != null) {
            return Result.error("-1", "当前已有申请记录，每学年仅可提交一条申请");
        }
        qgsqMapper.insert(qgsq);
        return Result.success();
    }


    //修改
    @PutMapping
    public Result<?> update(@RequestBody Qgsq qgsq) {
        qgsqMapper.updateById(qgsq);
        return Result.success();
    }


    //删除
    @DeleteMapping("/del/{id}")
    public Result<?> update(@PathVariable Long id) {
        qgsqMapper.deleteById(id);
        return Result.success();
    }


    //查询关键词username
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String college,
                              @RequestParam(defaultValue = "") String search_term,
                              @RequestParam(defaultValue = "") String search_name,
                              @RequestParam(defaultValue = "") String search_speed,
                              @RequestParam(defaultValue = "") String search_type) {
        LambdaQueryWrapper<Qgsq> wrapper = Wrappers.<Qgsq>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Qgsq::getUsername, search);
        }
        if (StrUtil.isNotBlank(college)) {
            wrapper.like(Qgsq::getCollege, college);
        }
        if (StrUtil.isNotBlank(search_term)) {
            wrapper.like(Qgsq::getSqxn, search_term);
        }
        if (StrUtil.isNotBlank(search_name)) {
            wrapper.like(Qgsq::getName, search_name);
        }
        if (StrUtil.isNotBlank(search_speed)) {
            wrapper.like(Qgsq::getSpeed, search_speed);
        }
        if (StrUtil.isNotBlank(search_type)) {
            wrapper.like(Qgsq::getType, search_type);
        }
        Page<Qgsq> qgsqPage = qgsqMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(qgsqPage);
    }

    //查询id
    @GetMapping("/{id}")
    public Result<?> search(@PathVariable Long id) {
        Qgsq res = qgsqMapper.selectOne(Wrappers.<Qgsq>lambdaQuery().eq(Qgsq::getId, id));
        return Result.success(res);
    }
}
