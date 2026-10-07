package com.expensetracker.config;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Component;
import javax.crypto.SecretKey; import java.util.Date;
@Component
public class JwtUtil {
  private final SecretKey key; private final long exp;
  public JwtUtil(@Value("${app.jwt.secret}") String s,@Value("${app.jwt.expiration-ms}") long exp){ key=Keys.hmacShaKeyFor(s.getBytes()); this.exp=exp; }
  public String generate(String email){ return Jwts.builder().subject(email).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+exp)).signWith(key).compact(); }
  public String parse(String t){ return Jwts.parser().verifyWith(key).build().parseSignedClaims(t).getPayload().getSubject(); }
}
