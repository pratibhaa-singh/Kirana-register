package com.kiranaregister.services;

import com.kiranaregister.config.HttpClientConfig;
import com.kiranaregister.dtos.Root;
import com.kiranaregister.dtos.TransactionDto;
import com.kiranaregister.dtos.TransactionResponse;
import com.kiranaregister.dtos.TransactionSubResponse;
import com.kiranaregister.exceptions.ApplicationException;
import com.kiranaregister.models.Account;
import com.kiranaregister.models.Transaction;
import com.kiranaregister.repositories.TransactionRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  @Autowired private HttpClientConfig httpClientConfig;

  @Autowired private TransactionRepository transactionRepository;

  @Autowired private AccountService accountService;

  @Value("${conversion.api}")
  private String currencyConversionAPI;

  public Transaction recordTransaction(TransactionDto transactionDto) {
    Transaction transaction = createTransaction(transactionDto);
    return transactionRepository.save(transaction);
  }

  private Transaction createTransaction(TransactionDto transactionDto) {
    Transaction transaction = new Transaction();
    Account account = accountService.fetchAccountById(transactionDto.getAccountId());
    transaction.setTransactionType(transactionDto.getTransactionType());
    transaction.setAmount(transactionDto.getAmount());
    transaction.setAccount(account);
    transaction.setCurrencyType(transactionDto.getCurrencyType());
    transaction.setPaymentMode(transactionDto.getPaymentMode());
    transaction.setIsDeleted(false);
    transaction.setNetAmount(calculateNetAmount(transactionDto, account));
    return transaction;
  }

  private Double calculateNetAmount(TransactionDto transactionDto, Account account) {
    Root root = httpClientConfig.getHttpRequestGetMethod(currencyConversionAPI);
    Map<String, Double> rates = root.getRates();
    Double supportedCoefficient = 1D;
    Double currencyCoefficient = 1D;
    if (!transactionDto.getCurrencyType().name().equalsIgnoreCase(root.base)) {
      currencyCoefficient = rates.get(transactionDto.getCurrencyType().name());
    }
    if (account.getSupportedCurrency().name().equalsIgnoreCase(root.base)) {
      supportedCoefficient = rates.get(account.getSupportedCurrency().name());
    }
    Double overAllCoefficient = supportedCoefficient / currencyCoefficient;
    return transactionDto.getAmount() * overAllCoefficient;
  }

  public TransactionResponse fetchTransaction(String date, Long accountId)
      throws ApplicationException {
    if (accountService.ifAccountExist(accountId)) {
      Timestamp startDate = Timestamp.valueOf(date);
      Timestamp endDate = new Timestamp(startDate.getTime() + (24 * 60 * 60 * 1000));
      return convertToTransactionResponse(
          transactionRepository.findByCreatedAtAndIsDeleted(startDate, endDate, false, accountId));
    } else {
      throw new ApplicationException(HttpStatus.NOT_FOUND, "Account Not Found");
    }
  }

  private TransactionResponse convertToTransactionResponse(List<Transaction> transactionList) {
    TransactionResponse transactionResponse = new TransactionResponse();
    transactionResponse.setSupportedCurrency(
        transactionList.get(0).getAccount().getSupportedCurrency());
    List<TransactionSubResponse> transactionSubResponseList = new ArrayList<>();
    Double totalCredit = 0D;
    Double totalDebit = 0D;
    for (Transaction transaction : transactionList) {
      if (transaction.getTransactionType().name().equalsIgnoreCase("CREDIT")) {
        totalCredit += transaction.getNetAmount();
      } else {
        totalDebit += transaction.getNetAmount();
      }
      transactionSubResponseList.add(
          new TransactionSubResponse(
              transaction.getAmount(),
              transaction.getCurrencyType(),
              transaction.getPaymentMode(),
              transaction.getTransactionType(),
              transaction.getNetAmount()));
    }
    transactionResponse.setTotalCreditAmount(totalCredit);
    transactionResponse.setTotalDebitAmount(totalDebit);
    transactionResponse.setTransactionSubResponseList(transactionSubResponseList);
    return transactionResponse;
  }

  public Transaction updateTransaction(Long transactionId, Double amount)
      throws ApplicationException {
    Transaction transaction =
        transactionRepository
            .findById(transactionId)
            .orElseThrow(
                () -> new ApplicationException(HttpStatus.NOT_FOUND, "Transaction Not Found"));
    Double currentAmount = transaction.getAmount();
    transaction.setAmount(amount);
    Double currentNetAmount = transaction.getNetAmount();
    transaction.setNetAmount((currentNetAmount / currentAmount) * amount);
    return transactionRepository.save(transaction);
  }

  public void deleteTransaction(Long transactionId) throws ApplicationException {
    if (transactionRepository.existsById(transactionId)) {
      transactionRepository.deleteById(transactionId);
    } else {
      throw new ApplicationException(HttpStatus.NOT_FOUND, "Transaction Not Found");
    }
  }
}
