package com.example.springtest.binding;

import org.springframework.web.util.HtmlUtils;

@ExecutesCommandValidation
public class DangerousInput {
  // SOURCE PROPERTY: Spring web binding writes the request's command field through setCommand.
  private String command;
  private Boolean active;
  private int priority;

  public String getCommand() {
    return command; // Step 3/0; Step 3/1; Step 3/2
  }

  public String getSummary() {
    return command; // Step 3/4
  }

  public String getEscapedCommand() {
    return HtmlUtils.htmlEscape(command);
  }

  public String getStatus() {
    return "ready";
  }

  public String getCommand(String prefix) {
    return prefix + command; // Step 3/5
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String commandText() {
    return command; // Step 3/3
  }

  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
