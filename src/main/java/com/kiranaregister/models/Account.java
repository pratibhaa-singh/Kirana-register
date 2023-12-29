package com.kiranaregister.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiranaregister.enums.CurrencyType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  @JsonIgnore
  private String password;

  @Column(name = "supported_currency", nullable = false)
  private CurrencyType supportedCurrency;
}
