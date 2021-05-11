package info.leochoi.creditservice.rule.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {
  @PreAuthorize("hasRole('ROLE_MANAGEMENT')")
  @PostMapping(
      path = "/creditservice/v1/rule/reload",
      consumes = "application/json",
      produces = "text/plain")
  public String reloadRules() {
    return "RELOADED";
  }
}
