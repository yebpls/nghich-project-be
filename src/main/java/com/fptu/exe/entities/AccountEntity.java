package com.fptu.exe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "account")
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "email")
  private String email;
  @Column(name = "password")
  private String password;
  @Column(name = "phone_number")
  private String phoneNumber;
  @Column(name = "role")
  private String role;
  @Column(name = "status")
  private boolean status;
}
