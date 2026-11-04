package com.example.springtest.security;

import java.io.IOException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityPrincipalController {
  @GetMapping("/principal")
  @PreAuthorize("hasRole('ADMIN')")
  public String runAsPrincipal(@AuthenticationPrincipal UserDetails principal) throws IOException { // Step 1/0
    Runtime.getRuntime().exec(principal.getUsername()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/0
    return "started";
  }

  @GetMapping("/context")
  @PreAuthorize("isAuthenticated()")
  public String runFromContext() throws IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Runtime.getRuntime().exec(authentication.getName()); // Inspection: java-basic-command-injection, taint.CMDi; Step 1/1
    return "started";
  }

  @GetMapping("/scope")
  public String keepSecurityPathsInScope(
    @org.springframework.web.bind.annotation.RequestParam String ignored
  ) throws IOException {
    runAsPrincipal(null);
    return runFromContext();
  }

  @GetMapping("/authentication")
  @PreAuthorize("isAuthenticated()")
  public String runFromAuthentication(
    @AuthenticationPrincipal Authentication authentication // Step 1/2
  ) throws IOException {
    if (authentication != null) {
      Runtime.getRuntime().exec(authentication.getName()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/2
    }
    return "started";
  }
}
