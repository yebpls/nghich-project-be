package com.fptu.exe.services;

import com.fptu.exe.entities.AccountEntity;

public interface LoginService {
  AccountEntity checkLogin(String email, String password);
}
