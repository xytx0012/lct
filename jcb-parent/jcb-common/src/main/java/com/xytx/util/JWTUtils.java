package com.xytx.util;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String SELF_KEY="sfafe123qwsx134ofi5938vifmr84ufj";

    public static String createJwt(Integer userId) {
        Key key=Keys.hmacShaKeyFor(SELF_KEY.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", userId);
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000*12);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .addClaims(claims)
                .compact();
    }

    public static Integer readJwt(String jwt) throws Exception{
        SecretKey secretKey = Keys.hmacShaKeyFor(SELF_KEY.getBytes(StandardCharsets.UTF_8));
        Claims body = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(jwt).getBody();
        Integer id =(Integer) body.get("uid");
        return id;
    }
}
