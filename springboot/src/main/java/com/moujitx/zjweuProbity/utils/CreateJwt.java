package com.moujitx.zjweuProbity.utils;

import com.moujitx.zjweuProbity.entity.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwt {
    public static String getoken(User user) {
        //Jwts.builder()生成
        //Jwts.parser()验证
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(user.getId() + "")
                .setSubject(user.getUsername())    //用户名
                .setIssuedAt(new Date())//登录时间
                .signWith(SignatureAlgorithm.HS256, "my-123").setExpiration(new Date(new
                        Date().getTime() + 86400000));
        //设置过期时间
        //前三个为载荷playload 最后一个为头部 header
        System.out.println(jwtBuilder.compact());
        return jwtBuilder.compact();
    }
}