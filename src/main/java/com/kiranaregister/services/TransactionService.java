package com.kiranaregister.services;

import com.kiranaregister.config.HttpClientConfig;
import com.kiranaregister.constants.Constants;
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
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

  /**
   * Create transaction entity and save indb
   * @param transactionDto
   * @return
   */
  public Transaction recordTransaction(TransactionDto transactionDto) {
    Transaction transaction = createTransaction(transactionDto);
    return transactionRepository.save(transaction);
  }

  /**
   * Create transaction entity from transaction dto
   * @param transactionDto
   * @return
   */
  private Transaction createTransaction(TransactionDto transactionDto) {
    Transaction transaction = new Transaction();
    Account account = accountService.fetchAccountById(transactionDto.getAccountId());
    BeanUtils.copyProperties(transactionDto,transaction);
    transaction.setAccount(account);
    transaction.setIsDeleted(Boolean.FALSE);
    transaction.setNetAmount(calculateNetAmountByBaseReference(transactionDto, account));
    return transaction;
  }

  /**
   * Fetch rates from third party and get amount in supported currency type
   * @param transactionDto
   * @param account
   * @return
   */
  private Double calculateNetAmountByBaseReference(TransactionDto transactionDto, Account account) {
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

  /**
   * Fetch transactions for date
   * @param date
   * @param accountId
   * @return
   * @throws ApplicationException
   */
  public TransactionResponse fetchTransaction(String date, Long accountId)
      throws ApplicationException {
    if (accountService.ifAccountExist(accountId)) {
      Timestamp startTimestamp = Timestamp.valueOf(date);
      Timestamp endTimestamp = new Timestamp(startTimestamp.getTime() + Constants.ONE_DAY_IN_MILLI_SECONDS);
      return convertToTransactionReport(
          transactionRepository.findByCreatedAtAndIsDeleted(startTimestamp, endTimestamp, false, accountId));
    } else {
      throw new ApplicationException(HttpStatus.NOT_FOUND, "Account Not Found");
    }
  }

  /**
   * Create transaction report from transaction list
   * @param transactionList
   * @return
   */
  private TransactionResponse convertToTransactionReport(List<Transaction> transactionList) {
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

  /**
   * soft delete transaction
   * @param transactionId
   * @throws ApplicationException
   */
  public void deleteTransaction(Long transactionId) throws ApplicationException {
    Optional<Transaction> existingTransactionOptional = transactionRepository.findById(transactionId);
    if (existingTransactionOptional.isPresent()) {
      Transaction transaction = existingTransactionOptional.get();
      transaction.setIsDeleted(Boolean.TRUE);
      transactionRepository.save(transaction);
    } else {
      throw new ApplicationException(HttpStatus.NOT_FOUND, "Transaction Not Found");
    }
  }
}
