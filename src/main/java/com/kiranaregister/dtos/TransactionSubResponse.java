package com.kiranaregister.dtos;

import com.kiranaregister.enums.CurrencyType;
import com.kiranaregister.enums.PaymentMode;
import com.kiranaregister.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSubResponse {

  private Double amount;

  private CurrencyType currencyType;

  private PaymentMode paymentMode;

  private TransactionType transactionType;

  private Double supportedCurrencyAmount;
}
