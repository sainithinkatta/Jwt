package com.example.tokengenerator.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {


    private String SECRET_KEY = "secret";

    //takes the token and return username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //takes the token and returns expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //it will figure what the claims are i.e the builder data
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    //it checks whether the token is expired or not
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //takes the userdetails object that are given by userdetailservices and create a JWT
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    //create token is called by above method and passes the claims
    //create token calls the jwt API (dependency) , uses a builder pattern
    //setsubject is the person who is successfully authenticated, username is passed as subject
    //issuedat is the currentdate and expiration is the time we set
    //signwith is used to sign the token(signature algo is used)
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }


    //checks whether username is same as userdetails
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
