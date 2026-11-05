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

  VariantEntity(String content) {
    this.content = content;
  }

  Long getId() {
    return id;
  }

  String getContent() {
    return content;
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
    return text;
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

  Long persist(VariantRequest request) {
    Long id = repository1.save(new VariantEntity(request.getContent())).getId();
    repository1.save(new VariantEntity("safe"));
    repository1.save(new OtherVariantEntity("safe"));
    return id;
  }

  Optional<VariantEntity> loadContent(Long id) {
    VariantRepository repository = repository1;
    VariantRepository alias = repository;
    VariantRepository selected = alias;
    return selected.findById(id);
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
    @RequestBody VariantRequest request
  ) {
    return ResponseEntity.ok(service.persist(request));
  }

  @GetMapping(value = "/examples/repository-variants/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> content(@PathVariable Long id) {
    VariantEntity entity = service.loadContent(id).orElseThrow();
    String content = entity.getContent();
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }

  @GetMapping(value = "/examples/repository-variants/{id}/other", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> contentFromOtherRepository(@PathVariable Long id) {
    String content = service.loadFromOtherRepository(id).getContent();
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }

  @GetMapping(value = "/examples/repository-variants/{id}/unrelated", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> unrelatedContent(@PathVariable Long id) {
    String content = service.loadUnrelated(id).getContent();
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
  }
}
