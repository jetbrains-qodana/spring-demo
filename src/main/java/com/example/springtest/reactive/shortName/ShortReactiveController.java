package com.example.springtest.reactive.shortName;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortReactiveController {
  @GetMapping("/examples/reactive/short-name")
  public String run(@RequestParam String command) throws IOException {
    String payload = Mono.just(command).block();
    Runtime.getRuntime().exec(payload);
    return "started";
  }
}
