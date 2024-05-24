package com.manager.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JwtUtils {

    //加密算法
    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    //私钥 / 生成签名的时候使用的秘钥secret，一般可以从本地配置文件中读取，切记这个秘钥不能外露，只在服务端使用，在任何场景都不应该流露出去。
    private final static String SECRET = "secretKey";

    // 过期时间（单位秒）/ 2小时
    private final static long ACCESS_TOKEN_EXPIRATION = 60 * 60 *2;

    //jwt签发者
    private final static String JWT_ISS = "lishuai";

    //主题（jwt所面向的用户）
    //private final static String SUBJECT = "all";


    /**
     * 根据用户ID生成token
     *
     * @param subject 主题（jwt所面向的用户）
     * @return
     */
    public static String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        //生成token
        return generateToken(claims);
    }


    /**
     * 根据用户ID生成token
     *
     * @param subject 主题（jwt所面向的用户）
     * @return
     */
    public static Map<String, String> generateTokenInfo(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        //生成token
        String token = generateToken(claims);
        //获取过期时间（时间戳）
        long expiredAt = getExpiredTime(token).getTime();
        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("token", token);
        tokenInfo.put("lifeTime", String.valueOf(getLifeTime()));
        tokenInfo.put("expiredAt", String.valueOf(expiredAt));
        return tokenInfo;
    }


    /**
     * 根据负载生成jwt token
     *
     * @param claims 负载
     * @return
     */
    public static String generateToken(Map<String, Object> claims) {
        // 头部 header / Jwt的头部承载，第一部分
        Map<String, Object> header = new HashMap<>();
        // 可不设置 默认格式是{"alg":"HS256"}
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        //标准中注册的声明 (建议但不强制使用),一旦写标准声明赋值之后，就会覆盖了那些标准的声明
        claims.put("iss", JWT_ISS);
            /*	iss: jwt签发者
                sub: jwt所面向的用户
                aud: 接收jwt的一方
                exp: jwt的过期时间，这个过期时间必须要大于签发时间
                nbf: 定义在什么时间之前，该jwt都是不可用的.
                iat: jwt的签发时间
                jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
            */

        //下面就是在为payload添加各种标准声明和私有声明了
        return Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                //头部信息
                .setHeader(header)
                //载荷信息
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //设置iat: jwt的签发时间
                .setIssuedAt(new Date())
                //设置exp：jwt过期时间
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000))
                //设置sub：代表这个jwt所面向的用户，所有人
                //.setSubject(SUBJECT)
                //设置签名：通过签名算法和秘钥生成签名
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                //开始压缩为xxxxx.yyyyy.zzzzz 格式的jwt token
                .compact();

    }

    /**
     * 解析token
     *
     * @param token 令牌
     * @return
     */
    public static String parseToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }


    /**
     * 从token中获取荷载
     *
     * @param token 令牌
     * @return
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 生成token失效时间
     *
     * @return
     */
    public static Date expirationDate() {
        //失效时间为：系统当前毫秒数+我们设置的时间（s）*1000=》毫秒
        return new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000);
    }

    /**
     * 校验token是否有效
     *
     * @param token  令牌
     * @param userId userId
     * @return
     */
    public static boolean validateToken(String token, String userId) {
        //判断token是否过期
        return Objects.equals(userId, parseToken(token)) && !isExpired(token);
    }


    /**
     * 校验token是否有效
     *
     * @param token 令牌
     * @return
     */
    public static boolean validateToken(String token) {
        //判断token是否过期
        return !isExpired(token);
    }

    /**
     * 判断token是否失效
     *
     * @param token 令牌
     * @return
     */
    public static boolean isExpired(String token) {
        Date expiredDate = getExpiredTime(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从荷载中获取过期日期
     *
     * @param token 令牌
     * @return
     */
    public static Date getExpiredTime(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否可以被刷新
     *
     * @param token 令牌
     * @return
     */
    public static boolean canBeRefreshed(String token) {
        return !isExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token 令牌
     * @return
     */
    public static String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        //修改为当前时间
        claims.setIssuedAt(new Date());
        //claims.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000));
        return generateToken(claims);
    }


    /**
     * 刷新token
     *
     * @param token 令牌
     * @return
     */
    public static Map<String, String> refreshTokenInfo(String token) {
        Claims claims = getClaimsFromToken(token);
        //修改为当前时间
        claims.setIssuedAt(new Date());
        //claims.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000));
        //生成token
        String newToken = generateToken(claims);
        //获取过期时间（时间戳）
        long expiredAt = getExpiredTime(newToken).getTime();

        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("token", newToken);
        tokenInfo.put("lifeTime", String.valueOf(getLifeTime()));
        tokenInfo.put("expiredAt", String.valueOf(expiredAt));
        return tokenInfo;
    }


    /**
     * 获取存活时间，单位秒
     *
     * @return
     */
    public static long getLifeTime() {
        return ACCESS_TOKEN_EXPIRATION;
    }

}
