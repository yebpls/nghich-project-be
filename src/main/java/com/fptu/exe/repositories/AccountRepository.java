package com.fptu.exe.repositories;

import com.fptu.exe.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
  AccountEntity findByEmail(String email);
}
