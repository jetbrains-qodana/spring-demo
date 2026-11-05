package com.example.springtest.annotations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

interface InheritedMappingApi {
  @GetMapping("/examples/annotations/inherited")
  String inherited(@RequestParam String command) throws IOException;
}

@RestController
public class InheritedMappingController implements InheritedMappingApi {
  @Override
  public String inherited(
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
