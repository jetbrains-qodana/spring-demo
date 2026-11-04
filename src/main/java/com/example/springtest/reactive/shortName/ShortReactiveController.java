package com.example.springtest.reactive.shortName;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortReactiveController {
  @GetMapping("/examples/reactive/short-name")
  public String run(@RequestParam String command) throws IOException { // Step 1/0
    String payload = Mono.just(command).block(); // Step 2/0
    Runtime.getRuntime().exec(payload); // Inspection: java-basic-command-injection, taint.CMDi; Step 3/0
    return "started";
  }
}
