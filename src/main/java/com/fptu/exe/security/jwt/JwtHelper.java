package com.fptu.exe.security.jwt;

import com.fptu.exe.entities.AccountEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
  @Value("${token.key}")
  private String strKey;

  private int expiredTime = 8 * 60 * 60 * 1000;

  public String generateToken(String role, AccountEntity account) {
    return generateToken(Map.of("role", role), account);
  }

  public String generateToken(Map<String, Object> extraClaims, AccountEntity account) {
    //Chuyển key đã lưu trữ từ BASE64 về SecretKey
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));
    Date date = new Date();
    long futureMilis = date.getTime() + expiredTime;
    Date futureDate = new Date(futureMilis);
    return Jwts.builder().setClaims(extraClaims)
        .expiration(futureDate)
        .claim("email", account.getEmail())
        .claim("id", account.getId())
        .signWith(key)
        .compact();
  }

  public String decodeToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));

    String data = null;
    try {
      data = Jwts.parser().verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
    } catch (Exception e) {
      System.out.println("Lỗi parse token: " + e.getMessage());
    }

    return data;
  }
}
