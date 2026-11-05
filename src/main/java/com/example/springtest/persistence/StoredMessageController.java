package com.example.springtest.persistence;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples/messages")
public class StoredMessageController {
  private final MessageService messageService;

  public StoredMessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public ResponseEntity<Long> create(
    @RequestBody MessageRequest request
  ) {
    return ResponseEntity.ok(messageService.save(request));
  }

  public ResponseEntity<Long> createWithoutAnnotations(MessageRequest request) {
    return ResponseEntity.ok(messageService.save(request));
  }

  @GetMapping(value = "/{id}/content", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> content(@PathVariable Long id) {
    MessageEntity message = messageService.find(id);
    String content = message.getContent();
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }

  public ResponseEntity<String> contentWithoutAnnotations(Long id) {
    MessageEntity message = messageService.find(id);
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(message.getContent());
  }

  @GetMapping(value = "/{id}/author", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> author(@PathVariable Long id) {
    MessageEntity message = messageService.find(id);
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(message.getAuthor());
  }
}
