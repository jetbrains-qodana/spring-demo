package org.springframework.security.core.context;

public final class SecurityContextHolder {
  private SecurityContextHolder() {
  }

  public static SecurityContext getContext() {
    throw new UnsupportedOperationException();
  }
}
