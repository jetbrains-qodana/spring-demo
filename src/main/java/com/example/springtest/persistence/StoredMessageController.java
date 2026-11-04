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
    // SOURCE: Spring deserializes attacker-controlled JSON into both fields of this request object.
    @RequestBody MessageRequest request // Step 1/0
  ) {
    return ResponseEntity.ok(messageService.save(request)); // Step 2/0; Step 14/0
  }

  public ResponseEntity<Long> createWithoutAnnotations(MessageRequest request) {
    // CONTROL: This ordinary method is not called with an HTTP body.
    return ResponseEntity.ok(messageService.save(request));
  }

  @GetMapping(value = "/{id}/content", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> content(@PathVariable Long id) {
    MessageEntity message = messageService.find(id); // Step 15/0; Step 17/0
    String content = message.getContent(); // Step 18/0; Step 20/0
    // SECURITY SINK: Stored request content reaches an HTML response during a later controller invocation.
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content); // Inspection: java-basic-xss, taint.XSS; Step 21/0
  }

  public ResponseEntity<String> contentWithoutAnnotations(Long id) {
    // CONTROL: Without a mapping, the returned value is not written to an HTTP response.
    MessageEntity message = messageService.find(id);
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(message.getContent());
  }

  @GetMapping(value = "/{id}/author", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> author(@PathVariable Long id) {
    MessageEntity message = messageService.find(id);
    // SAFE RESPONSE: The author field was HTML-escaped before storage, unlike the vulnerable content field.
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(message.getAuthor());
  }
}
