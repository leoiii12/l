package info.leochoi.creditservice.rule.service;

import info.leochoi.creditservice.rule.Rule;
import info.leochoi.creditservice.rule.config.InMemoryRuleConfig;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InMemoryScoringRuleServiceImpl implements ScoringRuleService {

  private static final Logger logger =
      LoggerFactory.getLogger(InMemoryScoringRuleServiceImpl.class);

  private final InMemoryRuleConfig inMemoryRuleConfig;

  public InMemoryScoringRuleServiceImpl(
      @Qualifier("inMemoryRuleConfig") InMemoryRuleConfig inMemoryRuleConfig) {
    this.inMemoryRuleConfig = inMemoryRuleConfig;
  }

  @Override
  @NotNull
  public List<Rule> getRules() {
    final List<Rule> rules = inMemoryRuleConfig.inMemoryRules();

    logger.info("Successfully retrieved the rules for calculations. numOfRules=[{}]", rules.size());

    return rules;
  }
}
