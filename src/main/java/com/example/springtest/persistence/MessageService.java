package com.example.springtest.persistence;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private final MessageRepository repository;

  public MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public Long save(MessageRequest request) { // Step 3/0
    MessageEntity entity = new MessageEntity(request.getContent(), request.getAuthor()); // Step 4/0; Step 6/0; Step 9/0
    MessageRepository loaded = repository; // Step 13/0
    MessageRepository alias = loaded; // Step 12/0
    MessageRepository selected = alias; // Step 11/0
    // PERSISTENCE WRITE: The repository preserves both fields beyond the lifetime of the POST request.
    return selected.save(entity).getId(); // Step 10/0
  }

  public MessageEntity find(Long id) {
    // PERSISTENCE READ: A later GET request can retrieve data that was supplied by an earlier caller.
    return repository.findById(id).orElseThrow(); // Step 16/0
  }
}
