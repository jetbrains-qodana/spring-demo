package com.example.springtest.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.web.util.HtmlUtils;

@Entity
public class MessageEntity {
  // PERSISTENCE ID: JPA assigns this identifier when the entity is saved.
  @Id
  @GeneratedValue
  private Long id;

  private String content;
  private String author;

  protected MessageEntity() {
  }

  public MessageEntity(String content, String author) { // Step 7/0
    // PROPAGATION: Request content is intentionally stored unchanged for the vulnerable read endpoint.
    this.content = content; // Step 8/0
    // SANITIZER: Only the author field is HTML-escaped before persistence, making that field safe for HTML.
    this.author = HtmlUtils.htmlEscape(author);
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content; // Step 19/0
  }

  public String getAuthor() {
    return author;
  }
}
