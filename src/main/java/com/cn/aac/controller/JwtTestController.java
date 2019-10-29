package com.cn.aac.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cn.aac.entity.User;
import com.cn.aac.jwtConfig.JWTUtils;
import com.cn.aac.jwtConfig.UserLoginToken;

import net.minidev.json.JSONObject;

@RestController(value = "/jwt")
public class JwtTestController {
    
    // 登陆
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        // 此处应从数据库验证用户是否存在。
        User DbUser = new User();
        DbUser.userName = "test";
        DbUser.password = "123456";
        // 验证通过返回token
        if (user != null && user.userName.equals(DbUser.userName) && user.password.equals(DbUser.password)) {
            jsonObject.put("msg", "success");
            // 获取token
            jsonObject.put("token", JWTUtils.getToken(user.userName, user.password));
        } else {
            jsonObject.put("msg", "user not find");
        }
        
        return jsonObject;
    }
    
    // 获取数据方法。验证token
    @UserLoginToken
    @PostMapping("/searchData")
    public Object searchData() {
        
        return "search Data success";
    }
}
