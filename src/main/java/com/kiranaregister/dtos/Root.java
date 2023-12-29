package com.kiranaregister.dtos;

import java.util.Date;
import java.util.Map;
import lombok.*;

@Getter
@Setter
public class Root {
  public boolean success;
  public String terms;
  public String privacy;
  public int timestamp;
  public Date date;
  public String base;
  public Map<String, Double> rates;
}
