package com.kiranaregister.config;

import com.kiranaregister.dtos.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClientConfig {

  @Autowired private RestTemplate restTemplate;

  public Root getHttpRequestGetMethod(String url) {
    return restTemplate.getForEntity(url, Root.class).getBody();
  }
}
