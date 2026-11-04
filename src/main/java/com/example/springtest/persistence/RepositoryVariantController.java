package com.example.springtest.persistence;

import java.util.Optional;
import jakarta.persistence.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

interface VariantRepository extends CrudRepository<VariantEntity, Long> {
}

@Entity
class VariantEntity {
  private Long id;
  private String content;

  VariantEntity(String content) { // Step 7/1
    this.content = content; // Step 8/1
  }

  Long getId() {
    return id;
  }

  String getContent() {
    return content; // Step 18/1
  }
}

@Entity
class OtherVariantEntity extends VariantEntity {
  OtherVariantEntity(String content) {
    super(content);
  }
}

class VariantRequest {
  private String text;

  String getContent() {
    return text; // Step 5/1
  }
}

@Service
class RepositoryVariantService {
  private final VariantRepository repository1;
  private final VariantRepository repository2;

  RepositoryVariantService(VariantRepository repository1, VariantRepository repository2) {
    this.repository1 = repository1;
    this.repository2 = repository2;
  }

  Long persist(VariantRequest request) { // Step 3/1
    Long id = repository1.save(new VariantEntity(request.getContent())).getId(); // Step 4/1; Step 6/1; Step 9/1
    repository1.save(new VariantEntity("safe"));
    repository1.save(new OtherVariantEntity("safe"));
    return id;
  }

  Optional<VariantEntity> loadContent(Long id) {
    VariantRepository repository = repository1; // Step 12/1
    VariantRepository alias = repository; // Step 13/1
    VariantRepository selected = alias; // Step 14/1
    return selected.findById(id); // Step 15/1
  }

  VariantEntity loadFromOtherRepository(Long id) {
    return repository2.findById(id).orElseThrow();
  }

  VariantEntity loadUnrelated(Long id) {
    repository1.findById(id).orElseThrow();
    return new VariantEntity("safe");
  }
}

@RestController
public class RepositoryVariantController {
  private final RepositoryVariantService service;

  public RepositoryVariantController(RepositoryVariantService service) {
    this.service = service;
  }

  @PostMapping("/examples/repository-variants")
  public ResponseEntity<Long> create(
    @RequestBody VariantRequest request // Step 1/1
  ) {
    return ResponseEntity.ok(service.persist(request)); // Step 2/1; Step 10/1
  }

  @GetMapping(value = "/examples/repository-variants/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> content(@PathVariable Long id) {
    VariantEntity entity = service.loadContent(id).orElseThrow(); // Step 11/1; Step 16/1
    String content = entity.getContent(); // Step 17/1; Step 19/1
    // SECURITY SINK: A later request reads content through service methods with common alternative names.
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content); // Inspection: java-basic-xss, taint.XSS; Step 20/1
  }

  @GetMapping(value = "/examples/repository-variants/{id}/other", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> contentFromOtherRepository(@PathVariable Long id) {
    String content = service.loadFromOtherRepository(id).getContent();
    // SAFE RESPONSE: The request write used repository1, but this read uses repository2.
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }

  @GetMapping(value = "/examples/repository-variants/{id}/unrelated", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> unrelatedContent(@PathVariable Long id) {
    String content = service.loadUnrelated(id).getContent();
    // SAFE RESPONSE: The service ignores the repository result and returns a new entity.
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }
}
