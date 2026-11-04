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
  public ResponseEntity<Long> commit(@RequestBody CommittedRequest request) { // Step 1/0
    return ResponseEntity.ok(service.saveCommitted(request)); // Step 2/0; Step 11/0
  }

  @PostMapping("/rollback")
  public ResponseEntity<Long> rollback(@RequestBody RolledBackRequest request) { // Step 1/1
    return ResponseEntity.ok(service.saveRolledBack(request)); // Step 2/1; Step 11/1
  }

  @GetMapping(value = "/commit/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> committed(@PathVariable Long id) {
    String value = service.findCommitted(id).getValue(); // Step 12/0; Step 14/0; Step 16/0
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
      .body(value); // Inspection: java-basic-xss, taint.XSS; Step 17/0
  }

  @GetMapping(value = "/rollback/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> rolledBack(@PathVariable Long id) {
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(service.findRolledBack(id).getValue()); // Inspection: java-basic-xss, taint.XSS; Step 12/1; Step 14/1; Step 16/1
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
  public Long saveCommitted(CommittedRequest request) { // Step 3/0
    CommittedEntity entity = new CommittedEntity(request.getValue()); // Step 4/0; Step 6/0; Step 9/0
    CommittedEntity saved = committedRepository.save(entity); // Step 10/0
    if (saved == null) throw new IllegalStateException("save failed");
    return saved.getId();
  }

  @Transactional
  public Long saveRolledBack(RolledBackRequest request) { // Step 3/1
    RolledBackEntity entity = new RolledBackEntity(request.getValue()); // Step 4/1; Step 6/1; Step 9/1
    rolledBackRepository.save(entity); // Step 10/1
    throw new IllegalStateException("roll back");
  }

  public CommittedEntity findCommitted(Long id) {
    return committedRepository.findById(id).orElseThrow(); // Step 13/0
  }

  public RolledBackEntity findRolledBack(Long id) {
    return rolledBackRepository.findById(id).orElseThrow(); // Step 13/1
  }
}

interface CommittedRepository extends CrudRepository<CommittedEntity, Long> {
}

interface RolledBackRepository extends CrudRepository<RolledBackEntity, Long> {
}
