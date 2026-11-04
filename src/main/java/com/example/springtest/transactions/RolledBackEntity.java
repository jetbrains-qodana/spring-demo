package com.example.springtest.transactions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RolledBackEntity {
  @Id
  @GeneratedValue
  private Long id;
  private String value;

  public RolledBackEntity(String value) { // Step 7/1
    this.value = value; // Step 8/1
  }

  public Long getId() {
    return id;
  }

  public String getValue() {
    return value; // Step 15/1
  }
}
