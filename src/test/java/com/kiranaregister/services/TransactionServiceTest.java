package com.kiranaregister.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.kiranaregister.config.HttpClientConfig;
import com.kiranaregister.dtos.Root;
import com.kiranaregister.dtos.TransactionDto;
import com.kiranaregister.enums.CurrencyType;
import com.kiranaregister.enums.PaymentMode;
import com.kiranaregister.enums.TransactionType;
import com.kiranaregister.exceptions.ApplicationException;
import com.kiranaregister.models.Account;
import com.kiranaregister.models.Transaction;
import com.kiranaregister.repositories.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock private HttpClientConfig httpClientConfig;

  @Mock private TransactionRepository transactionRepository;

  @Mock private AccountService accountService;

  @Mock private Root root;

  @Mock private Map<String, Double> rates;

  @InjectMocks private TransactionService transactionService;

  @Value("${conversion.api}")
  private String currencyConversionAPI;

  private TransactionDto transactionDto;

  private Account account;

  private Transaction transaction;

  private List<Transaction> transactionList;

  @BeforeEach
  public void setup() {
    transactionDto =
        new TransactionDto(1L, 10D, CurrencyType.INR, PaymentMode.CASH, TransactionType.DEBIT);
    account = new Account(1L, "email", "password", CurrencyType.INR);
    transaction =
        new Transaction(
            1L,
            account,
            10D,
            CurrencyType.INR,
            10D,
            PaymentMode.CASH,
            TransactionType.DEBIT,
            false);
    transactionList = new ArrayList<>();
    transactionList.add(transaction);
  }

  @Test
  void recordTransaction() {
    when(accountService.fetchAccountById(anyLong())).thenReturn(account);
    when(httpClientConfig.getHttpRequestGetMethod(currencyConversionAPI)).thenReturn(root);
    when(root.getRates()).thenReturn(rates);
    when(rates.get(anyString())).thenReturn(12D);
    when(transactionRepository.save(any())).thenReturn(transaction);
    assertEquals(transaction, transactionService.recordTransaction(transactionDto));
  }

  @Test
  void fetchTransaction() throws ApplicationException {
    when(accountService.ifAccountExist(anyLong())).thenReturn(true);
    when(transactionRepository.findByCreatedAtAndIsDeleted(any(), any(), any(), anyLong()))
        .thenReturn(transactionList);
    assertEquals(
        CurrencyType.INR,
        transactionService.fetchTransaction("2023-12-29 00:00:00", 1L).getSupportedCurrency());
  }

  @Test
  void updateTransaction() {}

  @Test
  void deleteTransaction() {}
}
