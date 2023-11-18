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
import com.moujitx.zjweuProbity.entity.User;
import com.moujitx.zjweuProbity.mapper.UserMapper;
import com.moujitx.zjweuProbity.utils.CreateJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    //@Resource
    //UserMapper userMapper;

    @Autowired
    private UserMapper userMapper;

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") String role,
                       @RequestParam(defaultValue = "") String username
    ) throws Exception {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like(User::getName, name);
        }
        if (StrUtil.isNotBlank(role)) {
            queryWrapper.like(User::getRole, role);
        }
        if (StrUtil.isNotBlank(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        List<User> list = userMapper.selectList(queryWrapper);         // 从数据库查询出所有的数据
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");    // 通过工具类创建writer 写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(true);        // 在内存操作，写出到浏览器
        //自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("role", "权限");
        writer.addHeaderAlias("username", "学号");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("person", "身份证号");
        writer.addHeaderAlias("male", "性别");
        writer.addHeaderAlias("nation", "民族");
        writer.addHeaderAlias("birth", "出生日期");
        writer.addHeaderAlias("politic", "政治面貌");
        writer.addHeaderAlias("province", "考区");
        writer.addHeaderAlias("graduate", "毕业学校");
        writer.addHeaderAlias("address", "通讯地址");
        writer.addHeaderAlias("admission", "入学日期");
        writer.addHeaderAlias("college", "学院");
        writer.addHeaderAlias("major", "专业");
        writer.addHeaderAlias("grade", "年级");
        writer.addHeaderAlias("classname", "班级");
        writer.addHeaderAlias("campus", "校区");
        writer.addHeaderAlias("level", "培养层次");
        writer.addHeaderAlias("phone", "联系电话");
        writer.addHeaderAlias("qq", "QQ号");
        writer.addHeaderAlias("type", "资助对象类别");
        writer.addHeaderAlias("status", "当前状态");
        writer.addHeaderAlias("detail", "备注");
        writer.addHeaderAlias("password", "密码");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息导出", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    //导入
    @PostMapping("/import")
    public Map<String, Object> imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        //List<User> list = reader.readAll(User.class);
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        int addNum = 0;
        int updNum = 0;
        for (List<Object> row : list) {
            User user = new User();
            String name = row.get(2).toString();
            user.setUsername(name);

            if (StrUtil.isNotBlank(row.get(1).toString())) user.setRole(row.get(1).toString());
            if (StrUtil.isNotBlank(row.get(3).toString())) user.setName(row.get(3).toString());
            if (StrUtil.isNotBlank(row.get(4).toString())) user.setPerson(row.get(4).toString());
            if (StrUtil.isNotBlank(row.get(5).toString())) user.setMale(row.get(5).toString());
            if (StrUtil.isNotBlank(row.get(6).toString())) user.setNation(row.get(6).toString());
            if (StrUtil.isNotBlank(row.get(7).toString())) user.setBirth(row.get(7).toString());
            if (StrUtil.isNotBlank(row.get(8).toString())) user.setPolitic(row.get(8).toString());
            if (StrUtil.isNotBlank(row.get(9).toString())) user.setProvince(row.get(9).toString());
            if (StrUtil.isNotBlank(row.get(10).toString())) user.setGraduate(row.get(10).toString());
            if (StrUtil.isNotBlank(row.get(11).toString())) user.setAddress(row.get(11).toString());
            if (StrUtil.isNotBlank(row.get(12).toString())) user.setAdmission(row.get(12).toString());
            if (StrUtil.isNotBlank(row.get(13).toString())) user.setCollege(row.get(13).toString());
            if (StrUtil.isNotBlank(row.get(14).toString())) user.setMajor(row.get(14).toString());
            if (StrUtil.isNotBlank(row.get(15).toString())) user.setGrade(row.get(15).toString());
            if (StrUtil.isNotBlank(row.get(16).toString())) user.setClassname(row.get(16).toString());
            if (StrUtil.isNotBlank(row.get(17).toString())) user.setCampus(row.get(17).toString());
            if (StrUtil.isNotBlank(row.get(18).toString())) user.setLevel(row.get(18).toString());
            if (StrUtil.isNotBlank(row.get(19).toString())) user.setPhone(row.get(19).toString());
            if (StrUtil.isNotBlank(row.get(20).toString())) user.setQq(row.get(20).toString());
            if (StrUtil.isNotBlank(row.get(21).toString())) user.setType(row.get(21).toString());
            if (StrUtil.isNotBlank(row.get(22).toString())) user.setStatus(row.get(22).toString());
            if (StrUtil.isNotBlank(row.get(23).toString())) user.setDetail(row.get(23).toString());

            if (StrUtil.isNotBlank(row.get(4).toString())) {
                user.setPassword(row.get(4).toString());
            } else {
                user.setPassword(name);
            }

            User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, name));
            if (res != null) {
                user.setId(res.getId());
                userMapper.updateById(user);
                updNum++;
            } else {
                userMapper.insert(user);
                addNum++;
            }
        }
        Map<String, Object> Result = new HashMap<>();
        Result.put("updNum", updNum);
        Result.put("addNum", addNum);
        return Result;
    }

    //查询全部
    @GetMapping("/")
    public List<User> index() {
        List<User> all = userMapper.findAll();
        return all;
    }


    //登录，获取token
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        Map<String, Object> Result = new HashMap<>();
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if (StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword())) {
            Result.put("code", 100);
            Result.put("msg", "用户名或密码不能为空");
        } else {
            if (res != null) {
                String token = CreateJwt.getoken(res);
                //Result.put("user",res);
                Result.put("token", token);
                Result.put("msg", "登录成功");
                Result.put("code", 200);
            } else {
                Result.put("code", "-1");
                Result.put("msg", "用户名或密码错误");
            }
        }
        return Result;
    }

    //token登录，先解析，后赋新值
    @PostMapping("/tokenLogin")
    public Object tokenSign(@RequestBody String token) {
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.isEmpty(token)) {
            map.put("code", 100);
            map.put("msg", "请求参数不能为空");
        } else {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("my-123")
                        .parseClaimsJws(token)
                        .getBody();
                Integer id = Integer.valueOf(claims.getId());
                System.out.println("用户id:" + claims.getId());
                System.out.println("用户名:" + claims.getSubject());
                System.out.println("用户时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                        format(claims.getIssuedAt()));
                System.out.println("过期时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                        format(claims.getExpiration()));
                String username = claims.getSubject();
                User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
                if (username != null && claims.getId() != null && username.equals(res.getUsername())) {
                    String newToken = CreateJwt.getoken(res);
                    map.put("user", res);
                    map.put("token", newToken);
                    map.put("msg", "登录成功");
                    map.put("code", 200);
                } else {
                    map.put("msg", "token错误");
                    map.put("code", 100);
                }
            } catch (Exception E) {
                map.put("msg", "登录异常，请重新登陆");
                map.put("code", 101);
            }
        }
        return map;
    }

    //退出
    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Result.success("退出成功");
    }


    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        userMapper.insert(user);
        return Result.success();
    }


    //新增
    @PostMapping
    public Result<?> save(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "工号重复");
        }
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }


    //修改
    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }


    //删除
    @DeleteMapping("/del/{id}")
    public Result<?> update(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }


    //查询关键词
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String role,
                              @RequestParam(defaultValue = "") String username) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getName, search);
        }
        if (StrUtil.isNotBlank(role)) {
            wrapper.like(User::getRole, role);
        }
        if (StrUtil.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    //查询id
    @GetMapping("/{id}")
    public Result<?> search(@PathVariable Long id) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        return Result.success(res);
    }

    //查询username
    @GetMapping("/username/{username}")
    public Result<?> search(@PathVariable String username) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        return Result.success(res);
    }


}
