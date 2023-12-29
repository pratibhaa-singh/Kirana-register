package com.kiranaregister.dtos;

import com.kiranaregister.enums.CurrencyType;
import com.kiranaregister.enums.PaymentMode;
import com.kiranaregister.enums.TransactionType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

  @NotNull(message = "Account id can't be null")
  private Long accountId;

  @NotNull(message = "Amount can't be null")
  private Double amount;

  @NotNull(message = "Supported currency can't be null")
  private CurrencyType currencyType;

  @NotNull(message = "Payment mode can't be null")
  private PaymentMode paymentMode;

  @NotNull(message = "Transaction type can't be null")
  private TransactionType transactionType;
}
