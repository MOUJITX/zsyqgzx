package com.moujitx.zjweuProbity.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moujitx.zjweuProbity.common.Result;
import com.moujitx.zjweuProbity.entity.Department;
import com.moujitx.zjweuProbity.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    //@Resource
    //DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Department> list = departmentMapper.findAll();         // 从数据库查询出所有的数据
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");    // 通过工具类创建writer 写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(true);        // 在内存操作，写出到浏览器
        //自定义标题别名
        writer.addHeaderAlias("number", "部门编号");
        writer.addHeaderAlias("name", "部门名称");
        writer.addHeaderAlias("detail", "部门说明");
        writer.addHeaderAlias("qtlxr", "钱塘校区联系人");
        writer.addHeaderAlias("qtdh", "钱塘校区联系电话");
        writer.addHeaderAlias("qtbgdz", "钱塘校区办公地址");
        writer.addHeaderAlias("qtrs", "钱塘校区岗位人数");
        writer.addHeaderAlias("nxlxr", "南浔校区联系人");
        writer.addHeaderAlias("nxdh", "南浔校区联系电话");
        writer.addHeaderAlias("nxbgdz", "南浔校区办公地址");
        writer.addHeaderAlias("nxrs", "南浔校区岗位人数");
        writer.addHeaderAlias("gwsm", "岗位说明");
        writer.addHeaderAlias("pytj", "用工需求");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("部门信息导出", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        //List<Department> list = reader.readAll(Department.class);
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Department> departments = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Department department = new Department();
            department.setNumber(row.get(1).toString());
            department.setName(row.get(2).toString());
            department.setDetail(row.get(3).toString());
            department.setQtlxr(row.get(4).toString());
            department.setQtdh(row.get(5).toString());
            department.setQtbgdz(row.get(6).toString());
            department.setQtrs(row.get(7).toString());
            department.setNxlxr(row.get(8).toString());
            department.setNxdh(row.get(9).toString());
            department.setNxbgdz(row.get(10).toString());
            department.setNxrs(row.get(11).toString());
            department.setGwsm(row.get(12).toString());
            department.setPytj(row.get(13).toString());
            departmentMapper.insert(department);
        }
        return true;
    }

    //查询全部
    @GetMapping("/")
    public List<Department> index() {
        List<Department> all = departmentMapper.findAll();
        return all;
    }


    //新增
    @PostMapping
    public Result<?> save(@RequestBody Department department) {
        Department res = departmentMapper.selectOne(Wrappers.<Department>lambdaQuery()
                .eq(Department::getNumber, department.getNumber()));
        if (res != null) {
            return Result.error("-1", "部门编号重复");
        }
        departmentMapper.insert(department);
        return Result.success();
    }


    //修改
    @PutMapping
    public Result<?> update(@RequestBody Department department) {
        departmentMapper.updateById(department);
        return Result.success();
    }


    //删除
    @DeleteMapping("/del/{id}")
    public Result<?> update(@PathVariable Long id) {
        departmentMapper.deleteById(id);
        return Result.success();
    }


    //查询关键词
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Department> wrapper = Wrappers.<Department>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Department::getName, search);
        }
        Page<Department> departmentPage = departmentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(departmentPage);
    }

    //查询id
    @GetMapping("/{id}")
    public Result<?> search(@PathVariable Long id) {
        Department res = departmentMapper.selectOne(Wrappers.<Department>lambdaQuery().eq(Department::getId, id));
        return Result.success(res);
    }


}
