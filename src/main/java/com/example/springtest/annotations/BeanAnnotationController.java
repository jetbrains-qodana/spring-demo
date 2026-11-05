package com.example.springtest.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Shows how a request value crosses Spring-managed collaborators before it reaches a process API.
 * The annotations select, configure, and inject the collaborators; none of them sanitize the value.
 */
@RestController
public class BeanAnnotationController {
  @Autowired
  @Qualifier("primaryCommandDecorator")
  private CommandDecorator commandDecorator;

  @Autowired
  private AnnotatedCommandRepository commandRepository;

  private final PlainCommandDecorator plainCommandDecorator = new PlainCommandDecorator();
  private final PlainCommandRepository plainCommandRepository = new PlainCommandRepository();

  @GetMapping("/examples/annotations/managed-beans")
  public String managedBeans(
    @RequestParam String command
  ) throws IOException {
    String decorated = commandDecorator.decorate(command);
    String stored = commandRepository.storeAndLoad(decorated);
    Runtime.getRuntime().exec(stored);
    return "command submitted";
  }

  public String managedBeansWithoutAnnotations(
    String command
  ) throws IOException {
    String decorated = plainCommandDecorator.decorate(command);
    String stored = plainCommandRepository.storeAndLoad(decorated);
    Runtime.getRuntime().exec(stored);
    return "command submitted";
  }
}

interface CommandDecorator {
  String decorate(String command);
}

@Component("primaryCommandDecorator")
@Primary
class AnnotatedCommandDecorator implements CommandDecorator {
  @Value("${springtest.command-prefix:}")
  private String prefix;

  @Override
  public String decorate(String command) {
    return prefix + command;
  }
}

class PlainCommandDecorator implements CommandDecorator {
  @Override
  public String decorate(String command) {
    return command;
  }
}

@Repository
class AnnotatedCommandRepository {
  public String storeAndLoad(String command) {
    return command;
  }
}

class PlainCommandRepository {
  public String storeAndLoad(String command) {
    return command;
  }
}
