package com.mai.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JwtUtils是个生成和校验jwt的工具类，其中有些jwt相关的密钥信息是从项目配置文件中配置的 <br />
 * 登录生成token 每次都携带token从vue返回springboot。token：出入证 <br />
 * 弹幕：<br />
 * - token是存在session或者redis中的，也就是在内存中速度快（本项目应该是存在 redis里）<br />
 * - 把token保存在客户端，这样服务端就是无状态的了，方便分布和扩展
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "mai.jwt")
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 生成jwt token
     */
    public String generateToken(String userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000); // *1000：秒换算成毫秒

        return Jwts.builder()
                // header
                .setHeaderParam("typ", "JWT")
                // payload
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                // signature
                .signWith(SignatureAlgorithm.HS512, secret) // 签名 加密算法
                .compact(); // 拼接
    }

    /**
     * 生成jwt token
     */
    public String generateToken(String userId, int setExpire) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + setExpire * 1000); // *1000：秒换算成毫秒

        return Jwts.builder()
                // header
                .setHeaderParam("typ", "JWT")
                // payload
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                // signature
                .signWith(SignatureAlgorithm.HS512, secret) // 签名 加密算法
                .compact(); // 拼接
    }


    /**
     * 获取jwt的值 <br />
     * 这个方法返回的Claims对象可以getXXX得到之前set进去的各种数值 <br />
     * 如getSubject()获得上面那个方法setSubject进去的userId
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret) // 解密
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
