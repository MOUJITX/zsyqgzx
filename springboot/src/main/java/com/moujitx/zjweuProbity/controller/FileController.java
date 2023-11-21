package com.moujitx.zjweuProbity.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FileController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @PostMapping("/upload")
    public Map<String, Object> upload(MultipartFile file, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String originName = file.getOriginalFilename();
        String format = sdf.format(new Date());
        ApplicationHome ah = new ApplicationHome(SpringBootApplication.class);  //获取jar存储位置
        String docStorePath = ah.getSource().getParentFile().toString();  //获取相对于jar存储位置的上级目录
        // String realPath = System.getProperty("user.dir") +"/springboot/src/main/resources/files/";
        String realPath = docStorePath + "/files/";
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String UniCodeName = UUID.randomUUID().toString();
        String newName = format + "_" + UniCodeName + "_" + originName;
        try {
            file.transferTo(new File(folder, newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/" + format + newName;
            String realUrl = "/api/" + UniCodeName;
            result.put("status", "success");
            result.put("realUrl", realUrl);
            result.put("dataUrl", UniCodeName);
        } catch (IOException e) {
            result.put("status", "error");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    //图片上传专用
    @PostMapping("/UploadImg")
    public Map<String, Object> UploadImg(MultipartFile file, HttpServletRequest req) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String originName = file.getOriginalFilename();
        if (!(originName.endsWith("jpg") || originName.endsWith("jpeg") || originName.endsWith("png")
                || originName.endsWith("bmp") || originName.endsWith("gif") || originName.endsWith("webp"))) {
            result.put("status", "error");
            result.put("msg", "文件类型错误，只允许上传图片格式");
            return result;
        }
        String format = sdf.format(new Date());
        ApplicationHome ah = new ApplicationHome(SpringBootApplication.class);  //获取jar存储位置
        String docStorePath = ah.getSource().getParentFile().toString();  //获取相对于jar存储位置的上级目录
        // String realPath = System.getProperty("user.dir") +"/springboot/src/main/resources/files/";
        String realPath = docStorePath + "/files/";
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String UniCodeName = UUID.randomUUID().toString();
        String newName = format + "_" + UniCodeName + "_" + originName;
        file.transferTo(new File(folder, newName));
        return JSONUtil.parseObj("{\"errno\":0,\"data\":[{url:\"" + "/" + "api" + "/" + UniCodeName + "\"}]}");
    }

    //视频上传专用
    @PostMapping("/UploadVideo")
    public Map<String, Object> UploadVideo(MultipartFile file, HttpServletRequest req) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String originName = file.getOriginalFilename();
        if (!(originName.endsWith("mp4") || originName.endsWith("wmv") || originName.endsWith("avi")
                || originName.endsWith("mpg") || originName.endsWith("webm") || originName.endsWith("mov"))) {
            result.put("status", "error");
            result.put("msg", "文件类型错误，只允许上传视频格式");
            return result;
        }
        String format = sdf.format(new Date());
        ApplicationHome ah = new ApplicationHome(SpringBootApplication.class);  //获取jar存储位置
        String docStorePath = ah.getSource().getParentFile().toString();  //获取相对于jar存储位置的上级目录
        // String realPath = System.getProperty("user.dir") +"/springboot/src/main/resources/files/";
        String realPath = docStorePath + "/files/";
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String UniCodeName = UUID.randomUUID().toString();
        String newName = format + "_" + UniCodeName + "_" + originName;
        file.transferTo(new File(folder, newName));
        return JSONUtil.parseObj("{\"errno\":0,\"data\":{url:\"" + "/" + "api" + "/" + UniCodeName + "\"}}");
    }

    //文件下载
    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;  // 新建一个输出流对象
        ApplicationHome ah = new ApplicationHome(SpringBootApplication.class);  //获取jar存储位置
        String docStorePath = ah.getSource().getParentFile().toString();  //获取相对于jar存储位置的上级目录
        // String realPath = System.getProperty("user.dir") +"/springboot/src/main/resources/files/";
        String basePath = docStorePath + "/files/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // 通过文件的路径读取文件字节流
                os = response.getOutputStream();   // 通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }
}
