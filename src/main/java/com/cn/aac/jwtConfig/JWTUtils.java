package com.cn.aac.jwtConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTUtils {
    
    public static String getToken(String userName, String passward) {
        String token = "";
        token = JWT.create().withAudience(userName).sign(Algorithm.HMAC256(passward));
        return token;
    }
    
}
