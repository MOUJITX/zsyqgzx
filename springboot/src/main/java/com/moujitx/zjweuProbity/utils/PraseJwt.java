package com.moujitx.zjweuProbity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

//token的解析
//有状态登录  服务器端保存用户信息
//无状态登录  服务器端没有保存用户信息   无状态效率比有状态效率高
public class PraseJwt {
    public static Claims tokenToOut(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("my-123")
                .parseClaimsJws(token)
                .getBody();
        System.out.println("用户id:" + claims.getId());
        System.out.println("用户名:" + claims.getSubject());
        System.out.println("用户时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getIssuedAt()));
        System.out.println("过期时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getExpiration()));
        System.out.println("用户角色:" + claims.get("role"));

        return claims;
    }
}
