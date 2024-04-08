package com.fptu.exe.security.jwt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  private JwtHelper jwtHelper;

  private Gson gson = new Gson();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
/**
 * B1: Lấy token từ giá trị header.
 * B2: Cắt chuỗi bỏ chữ Bearer đi.
 * B3: Giải mã token.
 * B4: Nếu giải mã thành công tạo ra Security Context Holder.
 * **/

    String bearerToken = request.getHeader("Authorization");
    Optional<String> tokenOptional = Optional.ofNullable(bearerToken);

//    Optional<String> bearerToken = Optional.ofNullable(request.getHeader("Authorization"));
    System.out.println("Kiem tra filter: " + tokenOptional);

//      bearerToken.filter(data -> data.startsWith("Bearer ")).isPresent()
    if (tokenOptional.isPresent()) {
//      String token = bearerToken.get().replace("Bearer ", "");
//      bearerToken.stream().map(data -> data.substring(7));
      String token = tokenOptional.get().substring(7);
      logger.info("token " + token);
      if (!token.isEmpty()) {
        String data = jwtHelper.decodeToken(token);
        //Tạo ra customType để Gson hỗ trợ parse JSON kiểu List
        Type listType = new TypeToken<List<SimpleGrantedAuthority>>() {
        }.getType();
        List<GrantedAuthority> listRoles = gson.fromJson(data, listType);
        logger.info("decode: " + data);
        //Tạo ContextHolder để bypass qua các filter của Security
        if (data != null) {
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken("", "", listRoles);
          SecurityContext securityContext = SecurityContextHolder.getContext();
          securityContext.setAuthentication(authenticationToken);
        }

      }

    }

    filterChain.doFilter(request, response);
  }
}
