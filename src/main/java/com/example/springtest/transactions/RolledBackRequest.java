package com.example.springtest.transactions;

public class RolledBackRequest {
  private String value;

  public String getValue() {
    return value; // Step 5/1
  }

  public void setValue(String value) {
    this.value = value;
  }
}
