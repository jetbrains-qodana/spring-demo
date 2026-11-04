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
    @RequestParam String command // Step 1/11
  ) throws IOException {
    // SECURITY SINK: Spring uses the mapping declared on the implemented controller method.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/11
    return "command submitted";
  }
}
