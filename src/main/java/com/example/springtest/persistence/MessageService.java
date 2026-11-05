package com.example.springtest.persistence;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private final MessageRepository repository;

  public MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public Long save(MessageRequest request) {
    MessageEntity entity = new MessageEntity(request.getContent(), request.getAuthor());
    MessageRepository loaded = repository;
    MessageRepository alias = loaded;
    MessageRepository selected = alias;
    return selected.save(entity).getId();
  }

  public MessageEntity find(Long id) {
    return repository.findById(id).orElseThrow();
  }
}
