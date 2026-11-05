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
    @RequestParam String value
  ) throws IOException {
    Runtime.getRuntime().exec(value);
    return Mono.just("command submitted");
  }

  public Mono<String> monoWithoutAnnotations(String value) throws IOException {
    Runtime.getRuntime().exec(value);
    return Mono.just("command submitted");
  }

  @GetMapping(value = "/examples/reactive/flux", produces = MediaType.TEXT_HTML_VALUE)
  public Flux<String> flux(
    @RequestParam String value
  ) throws IOException {
    Runtime.getRuntime().exec(value);
    return Flux.just("command submitted");
  }

  public Flux<String> fluxWithoutAnnotations(String value) throws IOException {
    Runtime.getRuntime().exec(value);
    return Flux.just("command submitted");
  }

  @GetMapping(value = "/examples/reactive/mono-payload", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> monoPayload(
    @RequestParam String value
  ) throws IOException {
    String payload = Mono.just(value).block();
    Runtime.getRuntime().exec(payload);
    return Mono.just(payload);
  }

  @GetMapping(value = "/examples/reactive/flux-payload", produces = MediaType.TEXT_HTML_VALUE)
  public Flux<String> fluxPayload(
    @RequestParam String value
  ) throws IOException {
    String payload = Flux.just(value).next().block();
    Runtime.getRuntime().exec(payload);
    return Flux.just(payload);
  }

  @GetMapping(value = "/examples/reactive/static-factory", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> staticFactoryResult() {
    String value = "command submitted";
    return staticWrap(value);
  }

  @GetMapping(value = "/examples/reactive/virtual-factory", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> virtualFactoryResult() {
    String value = "command submitted";
    return wrap(value);
  }

  @GetMapping(value = "/examples/reactive/empty", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> emptyPublisher() {
    return Mono.empty();
  }

  private static Mono<String> staticWrap(String value) {
    return Mono.just(value);
  }

  private Mono<String> wrap(String value) {
    return Mono.just(value);
  }
}
