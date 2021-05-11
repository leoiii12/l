package info.leochoi.creditservice.rule.service;

import info.leochoi.creditservice.rule.Rule;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ScoringRuleService {

  @NotNull
  List<Rule> getRules();
}
