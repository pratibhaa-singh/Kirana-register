package com.kiranaregister.controllers;

import com.kiranaregister.dtos.AccountDto;
import com.kiranaregister.dtos.Meta;
import com.kiranaregister.dtos.ResponseDto;
import com.kiranaregister.services.AccountService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/account")
public class AccountController {

  @Autowired private AccountService accountService;

  /**
   * POST API to create account
   * @param accountDto
   * @return
   */
  @PostMapping(path = "/create")
  public ResponseEntity<ResponseDto> createAccount(@RequestBody @Valid AccountDto accountDto) {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setData(accountService.createAccount(accountDto));
    responseDto.setMeta(new Meta("SUCCESS"));
    return ResponseEntity.ok(responseDto);
  }
}
