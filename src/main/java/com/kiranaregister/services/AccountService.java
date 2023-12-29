package com.kiranaregister.services;

import com.kiranaregister.dtos.AccountDto;
import com.kiranaregister.models.Account;
import com.kiranaregister.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired private AccountRepository accountRepository;

  /**
   * Create account entity and save in db
   * @param accountDto
   * @return
   */
  public Account createAccount(AccountDto accountDto) {
    Account account = createAccountFromDto(accountDto);
    return accountRepository.save(account);
  }

  /**
   * create account entity from account request dto
   * @param accountDto
   * @return
   */
  private Account createAccountFromDto(AccountDto accountDto) {
    Account account = new Account();
    account.setEmail(accountDto.getEmail());
    account.setPassword(accountDto.getPassword());
    account.setSupportedCurrency(accountDto.getSupportedCurrency());
    return account;
  }

  /**
   * fetch account by account id
   * @param id
   * @return
   */
  public Account fetchAccountById(Long id) {
    return accountRepository.findById(id).get();
  }

  /**
   * check if account exist
   * @param id
   * @return
   */
  public Boolean ifAccountExist(Long id) {
    return accountRepository.existsById(id);
  }
}
