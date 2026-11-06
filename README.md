# Spring Demo Project

Qodana for JVM includes a taint analysis that tracks untrusted data from where it enters an
application (a *source*) to a security-sensitive API (a *sink*). It reports the
vulnerabilities this flow enables: command injection, cross-site scripting (XSS), SQL injection,
path traversal, template injection, and open redirects.

This project is a minimal, self-contained Spring application that exercises the Spring features
the analysis must understand: request mapping and binding annotations, dependency injection,
bean configuration, persistence, messaging listeners, reactive types, and Spring Security.
Most vulnerable cases have a *control* next to them. A control is the same code without the
Spring wiring that makes it reachable, or with the value sanitized, so you can see exactly what
the analysis reports and what it does not.

## References

- [Taint analysis (Qodana documentation)](https://www.jetbrains.com/help/qodana/taint-analysis.html)
- [Qodana for JVM](https://www.jetbrains.com/help/qodana/jvm.html)
- [OWASP Top 10: Injection](https://owasp.org/Top10/A03_2021-Injection/)
- [OWASP: Cross-site scripting](https://owasp.org/www-community/attacks/xss/)

## Test cases

The cases live under [`src/main/java/com/example/springtest`](src/main/java/com/example/springtest),
one package per Spring feature:

| Package        | What it exercises                                                                                                      | Expected findings                                               |
|----------------|------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| `annotations`  | Every Spring MVC mapping and binding annotation, composed and inherited mappings, `@Controller` vs. `@RestController` | 12 command injection                                            |
| `binding`      | Form fields bound into model attributes, `@ModelAttribute` producers, `@Valid` with a custom validator                | 6 command injection                                             |
| `config`       | `@ConfigurationProperties`, `@Bean` factories, `@PostConstruct` lifecycle callbacks                                    | 2 template injection, 1 XSS                                     |
| `di`           | Constructor and field injection of a service                                                                           | 2 XSS                                                           |
| `messaging`    | Kafka, RabbitMQ, JMS, and application event listeners, including composed listener annotations                        | 11 command injection                                            |
| `persistence`  | Stored XSS: data saved through a Spring Data repository and returned by a later request                               | 2 XSS                                                           |
| `reactive`     | `Mono` and `Flux` handlers                                                                                             | 5 command injection, 2 XSS                                      |
| `report`       | A SQL query built across a service, a query factory, and a repository                                                 | 1 SQL injection                                                 |
| `security`     | `Authentication`, `Principal`, and `@AuthenticationPrincipal` as sources                                               | 7 command injection                                             |
| `sinks`        | Spring and JDK sinks: `JdbcTemplate`, SpEL parsing, `RedirectView`, file access                                       | 1 each: SQLi, template injection, open redirect, path traversal |
| `transactions` | `@Transactional` writes that commit or roll back                                                                       | 2 XSS                                                           |
| `web`          | Direct reflected XSS and command injection in a controller                                                             | 3 XSS, 1 command injection                                      |

In total, 61 findings are expected: 42 command injection, 12 XSS, 3 template injection,
2 SQL injection, 1 open redirect, and 1 path traversal.

## Configuration

[`qodana.yaml`](qodana.yaml) runs the taint analysis only, using the inspection profile in
[`taint-high.xml`](taint-high.xml). That profile sets the analysis depth to `HIGH`:

```xml
<inspection_tool class="JvmTaintAnalysis" enabled="true" level="WARNING" enabled_by_default="true">
  <option name="globalAnalysisDepth" value="HIGH" />
</inspection_tool>
```

The default depth is `LOW`. Two flows here pass through more beans than `LOW` follows, so
it misses them:
- the stored XSS in `persistence`;
- the SQL injection in `report`.

The project compiles against the real Spring and Reactor libraries from [`pom.xml`](pom.xml).
The one exception is the `KafkaTemplate` stub in
[`src/main/java/org/springframework/kafka/core`](src/main/java/org/springframework/kafka/core).
It keeps a one-argument `send(value)` that the `/kafka-default` case uses for the default topic.

## Running the analysis

Run the Qodana JVM linter with Docker from the project root. It picks up `qodana.yaml`
automatically:

```bash
docker run --rm -it \
    -p 8080:8080 \
    -e QODANA_TOKEN="$QODANA_TOKEN" \
    -v "$(pwd)":/data/project/ \
    -v "$(pwd)/.qodana/results":/data/results \
    jetbrains/qodana-jvm:latest \
    --show-report
```

## Expected traces

The [`benchmark-truth`](benchmark-truth) directory contains the ground truth for the project.
Its line numbers refer to the commit recorded in [`hash.txt`](benchmark-truth/hash.txt).

[`gold.csv`](benchmark-truth/gold.csv) has one row per expected finding: the vulnerability
category, the file, and the line of the sink.

```csv
rule,file,line
cmdi,src/main/java/com/example/springtest/annotations/BeanAnnotationController.java,37
sqli,src/main/java/com/example/springtest/report/ReportRepository.java,15
```

[`traces.json`](benchmark-truth/traces.json) holds the full expected traces. Each finding
lists:
- the inspection ids that should report it (OpenGrep `java-basic-*` and taint analysis `taint.*`);
- the sink;
- every step from source to sink.

```json
{
  "id": "com/example/springtest/annotations/0",
  "rules": ["java-basic-command-injection", "taint.CMDi"],
  "sink": { "file": "src/main/java/com/example/springtest/annotations/BeanAnnotationController.java", "line": 37 },
  "steps": [
    { "n": 1, "file": "src/main/java/com/example/springtest/annotations/BeanAnnotationController.java", "line": 33, "code": "@RequestParam String command" },
    { "n": 2, "file": "src/main/java/com/example/springtest/annotations/BeanAnnotationController.java", "line": 35, "code": "String decorated = commandDecorator.decorate(command);" }
  ]
}
```

Its `notes` list explains the cases line by line. The notes mark sources, sinks, sanitizers,
and Spring lifecycle callbacks. `CONTROL` and `SAFE RESPONSE` notes mark the negative cases,
which should not be reported.
