package com.cafe.management.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private String secret="btechdays";

    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }
    public boolean isTokeExpire(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final  String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokeExpire(token));
    }
    public <T> T extractClaims(String token, Function<Claims,T> claimResolver){
        final Claims claims=extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    public String generateToke(String username,String role){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",role);
        return createToke(claims,username);
    }

    public  String createToke(Map<String, Object> claims,String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }
}
