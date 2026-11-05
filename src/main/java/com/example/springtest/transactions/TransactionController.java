package com.example.springtest.transactions;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService service;

  public TransactionController(TransactionService service) {
    this.service = service;
  }

  @PostMapping("/commit")
  public ResponseEntity<Long> commit(@RequestBody CommittedRequest request) {
    return ResponseEntity.ok(service.saveCommitted(request));
  }

  @PostMapping("/rollback")
  public ResponseEntity<Long> rollback(@RequestBody RolledBackRequest request) {
    return ResponseEntity.ok(service.saveRolledBack(request));
  }

  @GetMapping(value = "/commit/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> committed(@PathVariable Long id) {
    String value = service.findCommitted(id).getValue();
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
      .body(value);
  }

  @GetMapping(value = "/rollback/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> rolledBack(@PathVariable Long id) {
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(service.findRolledBack(id).getValue());
  }
}

@Service
class TransactionService {
  private final CommittedRepository committedRepository;
  private final RolledBackRepository rolledBackRepository;

  TransactionService(CommittedRepository committedRepository, RolledBackRepository rolledBackRepository) {
    this.committedRepository = committedRepository;
    this.rolledBackRepository = rolledBackRepository;
  }

  @Transactional
  public Long saveCommitted(CommittedRequest request) {
    CommittedEntity entity = new CommittedEntity(request.getValue());
    CommittedEntity saved = committedRepository.save(entity);
    if (saved == null) throw new IllegalStateException("save failed");
    return saved.getId();
  }

  @Transactional
  public Long saveRolledBack(RolledBackRequest request) {
    RolledBackEntity entity = new RolledBackEntity(request.getValue());
    rolledBackRepository.save(entity);
    throw new IllegalStateException("roll back");
  }

  public CommittedEntity findCommitted(Long id) {
    return committedRepository.findById(id).orElseThrow();
  }

  public RolledBackEntity findRolledBack(Long id) {
    return rolledBackRepository.findById(id).orElseThrow();
  }
}

interface CommittedRepository extends CrudRepository<CommittedEntity, Long> {
}

interface RolledBackRepository extends CrudRepository<RolledBackEntity, Long> {
}
