package com.example.springtest.binding;

import org.springframework.web.util.HtmlUtils;

@ExecutesCommandValidation
public class DangerousInput {
  private String command;
  private Boolean active;
  private int priority;

  public String getCommand() {
    return command;
  }

  public String getSummary() {
    return command;
  }

  public String getEscapedCommand() {
    return HtmlUtils.htmlEscape(command);
  }

  public String getStatus() {
    return "ready";
  }

  public String getCommand(String prefix) {
    return prefix + command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String commandText() {
    return command;
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
