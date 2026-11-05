package com.example.springtest.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.web.util.HtmlUtils;

@Entity
public class MessageEntity {
  @Id
  @GeneratedValue
  private Long id;

  private String content;
  private String author;

  protected MessageEntity() {
  }

  public MessageEntity(String content, String author) {
    this.content = content;
    this.author = HtmlUtils.htmlEscape(author);
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public String getAuthor() {
    return author;
  }
}
