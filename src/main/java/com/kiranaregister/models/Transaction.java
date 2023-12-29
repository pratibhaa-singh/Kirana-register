package com.kiranaregister.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiranaregister.enums.CurrencyType;
import com.kiranaregister.enums.PaymentMode;
import com.kiranaregister.enums.TransactionType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id", name = "account_id", nullable = false)
  @JsonIgnore
  private Account account;

  @Column(name = "amount", nullable = false)
  private Double amount;

  @Column(name = "currency_type", nullable = false)
  private CurrencyType currencyType;

  @Column(name = "net_amount", nullable = false)
  private Double netAmount;

  @Column(name = "payment_mode", nullable = false)
  private PaymentMode paymentMode;

  @Column(name = "transaction_type", nullable = false)
  private TransactionType transactionType;

  @Column(name = "is_deleted", nullable = false)
  @JsonIgnore
  private Boolean isDeleted;
}
