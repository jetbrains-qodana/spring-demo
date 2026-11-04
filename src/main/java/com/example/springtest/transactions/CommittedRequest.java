package com.example.springtest.transactions;

public class CommittedRequest {
  private String value;

  public String getValue() {
    return value; // Step 5/0
  }

  public void setValue(String value) {
    this.value = value;
  }
}
