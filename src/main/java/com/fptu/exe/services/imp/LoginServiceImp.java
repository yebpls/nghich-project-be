package com.fptu.exe.services.imp;

import com.fptu.exe.entities.AccountEntity;
import com.fptu.exe.repositories.AccountRepository;
import com.fptu.exe.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements LoginService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AccountRepository accountRepository;

  private Logger logger = LoggerFactory.getLogger(LoginServiceImp.class);
  @Override
  public AccountEntity checkLogin(String email, String password) {
    AccountEntity account = accountRepository.findByEmail(email);
//    String endcodePass = passwordEncoder.encode(password);

    if(account != null && passwordEncoder.matches(password, account.getPassword()) && account.isStatus()){
      return account;
    } else if (account != null && passwordEncoder.matches(password, account.getPassword()) && !account.isStatus()) {
      return account;
    }

    System.out.println(account);
    return null;
  }
}
