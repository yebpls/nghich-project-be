package com.fptu.exe.security;

import com.fptu.exe.entities.AccountEntity;
import com.fptu.exe.services.LoginService;
import com.fptu.exe.services.imp.LoginServiceImp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenProvider implements AuthenticationProvider {

  @Autowired
  private LoginService loginService;

  // Nếu trả ra 1 Authentication là đăng nhập thành công, null là thất bại
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    System.out.println("Kiểm tra chứng thực " + email + " " + password);
    AccountEntity account = loginService.checkLogin(email, password);
    if (account != null) {

      //Tạo 1 list nhận vào danh sách quyền theo chuẩn Security
      List<GrantedAuthority> listRoles = new ArrayList<>();
      //Ta ra 1 quyêền và gán tên quyền để dadd vào listrole ở trên
      SimpleGrantedAuthority role = new SimpleGrantedAuthority(account.getRole());
      listRoles.add(role);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          "", "", listRoles);
      return authenticationToken;
    }

    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
