package com.example.springtest.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class ReactiveController {
  @GetMapping(value = "/examples/reactive/mono", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> mono(
    // SOURCE: Spring obtains this value from the caller's query string.
    @RequestParam String value // Step 1/0
  ) throws IOException {
    // SECURITY SINK: A reactive return type does not make request data safe for process creation.
    Runtime.getRuntime().exec(value); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/0
    return Mono.just("command submitted");
  }

  public Mono<String> monoWithoutAnnotations(String value) throws IOException {
    // CONTROL: This ordinary method is not a reactive HTTP handler and has no request-derived argument.
    Runtime.getRuntime().exec(value);
    return Mono.just("command submitted");
  }

  @GetMapping(value = "/examples/reactive/flux", produces = MediaType.TEXT_HTML_VALUE)
  public Flux<String> flux(
    // SOURCE: Spring obtains this value from the caller's query string.
    @RequestParam String value // Step 1/1
  ) throws IOException {
    // SECURITY SINK: The request value reaches process creation before the Flux response is returned.
    Runtime.getRuntime().exec(value); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/1
    return Flux.just("command submitted");
  }

  public Flux<String> fluxWithoutAnnotations(String value) throws IOException {
    // CONTROL: With no mapping or binding annotation, this duplicate body is not a request callback.
    Runtime.getRuntime().exec(value);
    return Flux.just("command submitted");
  }

  @GetMapping(value = "/examples/reactive/mono-payload", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> monoPayload(
    @RequestParam String value // Step 1/2; Step 1/4
  ) throws IOException {
    String payload = Mono.just(value).block(); // Step 2/2; Step 2/4
    // SECURITY SINK: The Mono preserves the command taint of its input.
    Runtime.getRuntime().exec(payload); // Inspection: java-basic-command-injection, taint.CMDi; Step 3/2
    return Mono.just(payload); // Inspection: java-basic-xss, taint.XSS; Step 3/4
  }

  @GetMapping(value = "/examples/reactive/flux-payload", produces = MediaType.TEXT_HTML_VALUE)
  public Flux<String> fluxPayload(
    @RequestParam String value // Step 1/3; Step 1/5
  ) throws IOException {
    String payload = Flux.just(value).next().block(); // Step 2/3; Step 2/5
    // SECURITY SINK: The Flux and Mono preserve the command taint of their input.
    Runtime.getRuntime().exec(payload); // Inspection: java-basic-command-injection, taint.CMDi; Step 3/3
    return Flux.just(payload); // Inspection: java-basic-xss, taint.XSS; Step 3/5
  }

  @GetMapping(value = "/examples/reactive/static-factory", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> staticFactoryResult() {
    // CONTROL: The model reads the reactive return type of a static call with a non-reactive owner.
    String value = "command submitted";
    return staticWrap(value);
  }

  @GetMapping(value = "/examples/reactive/virtual-factory", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> virtualFactoryResult() {
    // CONTROL: The model reads the reactive return type of a virtual call on a non-reactive receiver.
    String value = "command submitted";
    return wrap(value);
  }

  @GetMapping(value = "/examples/reactive/empty", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> emptyPublisher() {
    // CONTROL: A publisher without an input does not create a taint value.
    return Mono.empty();
  }

  private static Mono<String> staticWrap(String value) {
    return Mono.just(value);
  }

  private Mono<String> wrap(String value) {
    return Mono.just(value);
  }
}
