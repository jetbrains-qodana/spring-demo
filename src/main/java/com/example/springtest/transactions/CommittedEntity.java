package com.example.springtest.transactions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CommittedEntity {
  @Id
  @GeneratedValue
  private Long id;
  private String value;

  public CommittedEntity(String value) { // Step 7/0
    this.value = value; // Step 8/0
  }

  public Long getId() {
    return id;
  }

  public String getValue() {
    return value; // Step 15/0
  }
}
