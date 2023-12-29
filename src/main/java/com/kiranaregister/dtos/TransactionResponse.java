package com.kiranaregister.dtos;

import com.kiranaregister.enums.CurrencyType;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TransactionResponse implements Serializable {

  private List<TransactionSubResponse> transactionSubResponseList;

  private CurrencyType supportedCurrency;

  private Double totalDebitAmount;

  private Double totalCreditAmount;
}
