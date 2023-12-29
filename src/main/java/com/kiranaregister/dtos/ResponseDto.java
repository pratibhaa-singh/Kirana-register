package com.kiranaregister.dtos;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto implements Serializable {
  private Object data;
  private Meta meta;
}
