package com.kiranaregister.controllers;

import com.kiranaregister.dtos.Meta;
import com.kiranaregister.dtos.ResponseDto;
import com.kiranaregister.dtos.TransactionDto;
import com.kiranaregister.exceptions.ApplicationException;
import com.kiranaregister.services.TransactionService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/transactions")
@Validated
public class TransactionController {

  @Autowired private TransactionService transactionService;

  @PostMapping(path = "/")
  public ResponseEntity<ResponseDto> recordTransaction(
      @RequestBody @Valid TransactionDto transactionDto) {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setData(transactionService.recordTransaction(transactionDto));
    responseDto.setMeta(new Meta("SUCCESS"));
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping(path = "/")
  public ResponseEntity<ResponseDto> fetchTransaction(
      @RequestParam(name = "date") @DateTimeFormat(pattern = "yyy-mm-dd hh:mm:ss") @Valid
          String date,
      @RequestParam(name = "accountId") @NotNull(message = "Account id can't be null") @Valid
          Long accountId)
      throws ApplicationException {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setData(transactionService.fetchTransaction(date, accountId));
    responseDto.setMeta(new Meta("SUCCESS"));
    return ResponseEntity.ok(responseDto);
  }

  @PutMapping(path = "/{transactionId}")
  public ResponseEntity<ResponseDto> updateTransaction(
      @PathVariable(name = "transactionId")
          @NotNull(message = "Transaction id can't be null")
          @Valid
          Long transactionId,
      @RequestParam(name = "amount") @NotNull(message = "Amount can't be null") @Valid
          Double amount)
      throws ApplicationException {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setData(transactionService.updateTransaction(transactionId, amount));
    responseDto.setMeta(new Meta("SUCCESS"));
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping(path = "/{transactionId}")
  public ResponseEntity<ResponseDto> deleteTransaction(
      @PathVariable(name = "transactionId")
          @NotNull(message = "Transaction id can't be null")
          @Valid
          Long transactionId)
      throws ApplicationException {
    ResponseDto responseDto = new ResponseDto();
    transactionService.deleteTransaction(transactionId);
    responseDto.setMeta(new Meta("DELETED"));
    return ResponseEntity.ok(responseDto);
  }
}
