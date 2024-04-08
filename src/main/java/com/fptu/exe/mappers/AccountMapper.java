package com.fptu.exe.mappers;

import com.fptu.exe.DTO.AccountDTO;
import com.fptu.exe.entities.AccountEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  private ModelMapper modelMapper;

  public AccountDTO convertToDTO(AccountEntity account){
    AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
    return accountDTO;
  }

  public AccountEntity revertToEntity(AccountDTO accountDTO){
    AccountEntity account = modelMapper.map(accountDTO, AccountEntity.class);

    return account;
  }
}
