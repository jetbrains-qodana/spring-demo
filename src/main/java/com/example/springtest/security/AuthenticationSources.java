package com.example.springtest.security;

import java.io.IOException;
import java.security.Principal;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationSources {
  public void nameFromContext() throws IOException {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var name = authentication.getName();
    Runtime.getRuntime().exec(name); // Inspection: java-basic-command-injection, taint.CMDi
  }

  public void principalFromContext() throws IOException {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var principal = (String)authentication.getPrincipal();
    Runtime.getRuntime().exec(principal); // Inspection: java-basic-command-injection, taint.CMDi
  }

  public void nameFromSubtype(ExtendedAuthentication authentication) throws IOException {
    var name = authentication.getName();
    Runtime.getRuntime().exec(name); // Inspection: java-basic-command-injection, taint.CMDi
  }

  public void principalFromSubtype(ExtendedAuthentication authentication) throws IOException {
    var principal = (String)authentication.getPrincipal();
    Runtime.getRuntime().exec(principal); // Inspection: java-basic-command-injection, taint.CMDi
  }

  public void unrelated(Principal principal, Authentication authentication) throws IOException {
    Runtime.getRuntime().exec(principal.getName());
    Runtime.getRuntime().exec(authentication.getName());
    Runtime.getRuntime().exec((String)authentication.getPrincipal());
  }

  public interface ExtendedAuthentication extends org.springframework.security.core.Authentication {
  }

  public interface Authentication {
    String getName();
    Object getPrincipal();
  }
}
