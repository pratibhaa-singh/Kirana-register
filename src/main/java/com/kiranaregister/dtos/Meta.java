package com.kiranaregister.dtos;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta implements Serializable {

  private String responseMessage;

  public Meta(String responseMessage) {
    this.responseMessage = responseMessage;
  }
}
