package com.fptu.exe.DTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AccountDTO {
  private Integer id;
  private String email;
  private String password;
  private String phoneNumber;
  private String role;
  private boolean status;
}
