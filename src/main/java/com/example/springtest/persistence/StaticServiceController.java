package com.example.springtest.persistence;

import jakarta.persistence.Entity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class StaticRequest {
  private String value;

  String getValue() {
    return value;
  }
}

@Entity
class StaticEntity {
  private final String value;

  StaticEntity(String value) {
    this.value = value;
  }

  String getValue() {
    return value;
  }
}

@Service
class StaticService {
  static StaticEntity save(StaticRequest request) {
    return new StaticEntity(request.getValue());
  }

  void save() {
  }
}

@RestController
public class StaticServiceController {
  @PostMapping("/examples/static-service")
  public void create(@RequestBody StaticRequest request) {
    StaticService.save(request);
  }

  @GetMapping(value = "/examples/static-service", produces = MediaType.TEXT_HTML_VALUE)
  public String read() {
    // SAFE RESPONSE: The read does not use repository state from the earlier request.
    return new StaticEntity("stored").getValue();
  }
}
