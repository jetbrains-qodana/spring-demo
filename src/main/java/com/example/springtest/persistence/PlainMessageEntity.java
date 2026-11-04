package com.example.springtest.persistence;

/**
 * Structural control for {@link MessageEntity}: the fields and accessors are the same, but this class has no
 * entity or identifier annotations, so Spring Data does not treat it as persisted application state.
 */
public class PlainMessageEntity {
  private Long id;
  private String content;
  private String author;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}
