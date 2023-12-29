package com.kiranaregister.dtos;

import com.kiranaregister.enums.CurrencyType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class AccountDto {

  @NotEmpty(message = "Email can't be null or empty")
  @Email(message = "Email is not valid")
  private String email;

  @NotEmpty(message = "Password can't be null or empty")
  private String password;

  @NotNull(message = "Supported currency can't be null")
  private CurrencyType supportedCurrency;
}
