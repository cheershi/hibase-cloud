package com.hibase.baseweb.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;

import java.util.Date;

/**
 * jwt工具类
 *
 * @Author hufeng
 * @Time 2018/12/13 13:29
 */
public class JWTUtil {

    /**
     * 生成 token
     *
     * @return 加密的token
     */
    public static String createToken(TokenProperties tokenProperties) {
        Date date = MyUtils.addTimeBySecond(MyUtils.generateTime(), tokenProperties.getExpireTime());

        Algorithm algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
        // 附带username信息
        return JWT.create()
                .withClaim("username", tokenProperties.getUsername())
                //到期时间
                .withExpiresAt(date)
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
    }

    /**
     * 校验 token 是否正确
     *
     * @return 是否正确
     */
    public static boolean verify(TokenProperties tokenProperties) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", tokenProperties.getUsername())
                    .build();
            //验证 token
            verifier.verify(tokenProperties.getToken());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getPassword(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("password").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    @Data
    public static class TokenProperties {

        private String username;

        private String token;

        private String password;

        private String secret;

        private int expireTime;
    }

    public static void main(String[] args) {

        TokenProperties tokenProperties = new TokenProperties();
        tokenProperties.setUsername("admin");
        tokenProperties.setPassword("admin");
        tokenProperties.setSecret("jwtSecret");
        tokenProperties.setExpireTime(864000);
        tokenProperties.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6ImFkbWluIiwiZXhwIjoxNTQ2ODMxMTk3LCJ1c2VybmFtZSI6ImFkbWluIn0.vMyPY3kvwPCJmgQ7t68hib7dN8SMt10FBr2pwjHyqVQ");

        System.out.println(createToken(tokenProperties));

        //System.out.println(JWTUtil.verify(tokenProperties));
    }
}
