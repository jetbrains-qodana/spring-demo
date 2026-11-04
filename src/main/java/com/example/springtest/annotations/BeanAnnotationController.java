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
    @RequestParam String command // Step 1/0
  ) throws IOException {
    // SOURCE: An HTTP client controls the command query parameter.
    String decorated = commandDecorator.decorate(command); // Step 2/0; Step 5/0
    String stored = commandRepository.storeAndLoad(decorated); // Step 6/0; Step 9/0
    // SECURITY SINK: Taint survives component selection, field injection, configuration, and repository calls.
    Runtime.getRuntime().exec(stored); // Inspection: java-basic-command-injection, taint.CMDi; Step 10/0
    return "command submitted";
  }

  public String managedBeansWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: These plain objects and this ordinary method have the same Java data flow but no request entry point.
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
    // PROPAGATION: A configuration prefix changes presentation but does not remove dangerous command syntax.
    return prefix + command;
  }
}

class PlainCommandDecorator implements CommandDecorator {
  @Override
  public String decorate(String command) { // Step 3/0
    return command; // Step 4/0
  }
}

@Repository
class AnnotatedCommandRepository {
  public String storeAndLoad(String command) { // Step 7/0
    // PROPAGATION: Repository stereotypes indicate persistence responsibility, not input sanitization.
    return command; // Step 8/0
  }
}

class PlainCommandRepository {
  public String storeAndLoad(String command) {
    return command;
  }
}
