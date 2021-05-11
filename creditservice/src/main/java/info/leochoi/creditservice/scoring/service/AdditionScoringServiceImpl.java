package info.leochoi.creditservice.scoring.service;

import info.leochoi.creditservice.rule.CompanyTypeRule;
import info.leochoi.creditservice.rule.NumberOfEmployeesRule;
import info.leochoi.creditservice.rule.NumberOfYearsOperatedRule;
import info.leochoi.creditservice.rule.Rule;
import info.leochoi.creditservice.rule.service.ScoringRuleService;
import info.leochoi.creditservice.scoring.ScoringInput;
import info.leochoi.creditservice.scoring.exception.NoRuleIsMatchedException;
import info.leochoi.creditservice.snapshot.SnapshotService;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdditionScoringServiceImpl implements ScoringService {

  private static final Logger logger = LoggerFactory.getLogger(AdditionScoringServiceImpl.class);

  private final ScoringRuleService scoringRuleService;
  private final SnapshotService snapshotService;

  public AdditionScoringServiceImpl(
      ScoringRuleService scoringRuleService, SnapshotService snapshotService) {
    this.scoringRuleService = scoringRuleService;
    this.snapshotService = snapshotService;
  }

  /**
   * This gathers the rules from {@link ScoringRuleService}, and takes those rules to calculate the
   * score. This implementation allows for each type only one {@link NumberOfEmployeesRule}, {@link
   * CompanyTypeRule}, {@link NumberOfYearsOperatedRule}.
   *
   * <p>In future, there may be some rules in the same type that can be applied multiple times. We
   * may add isExclusive to the {@link Rule} to define whether it is exactly once. We may also
   * change the way to reduce the rules.
   *
   * @param scoringInput
   * @return the calculated score
   */
  @Override
  public long calculateScore(final @NotNull ScoringInput scoringInput) {
    logger.info("Start calculate the score of the scoringInput=[{}].", scoringInput);

    final List<Rule> rules = scoringRuleService.getRules();

    final List<Rule> applicableNumberOfEmployeesRules =
        rules.stream()
            .filter(r -> r instanceof NumberOfEmployeesRule)
            .filter(r -> r.test(scoringInput))
            .collect(Collectors.toUnmodifiableList());
    if (applicableNumberOfEmployeesRules.size() != 1) {
      logger.error("Unable to match the NumberOfEmployeesRule. Exactly one one should be matched.");
      throw new NoRuleIsMatchedException("NumberOfEmployeesRule");
    }

    final List<Rule> applicableCompanyTypeRules =
        rules.stream()
            .filter(r -> r instanceof CompanyTypeRule)
            .filter(r -> r.test(scoringInput))
            .collect(Collectors.toUnmodifiableList());
    if (applicableCompanyTypeRules.size() != 1) {
      logger.error("Unable to match the CompanyTypeRule. Exactly one should be matched.");
      throw new NoRuleIsMatchedException("CompanyTypeRule");
    }

    final List<Rule> applicableNumberOfYearsOperatedRules =
        rules.stream()
            .filter(r -> r instanceof NumberOfYearsOperatedRule)
            .filter(r -> r.test(scoringInput))
            .collect(Collectors.toUnmodifiableList());
    if (applicableNumberOfYearsOperatedRules.size() != 1) {
      logger.error("Unable to match the NumberOfYearsOperatedRule. Exactly one should be matched.");
      throw new NoRuleIsMatchedException("NumberOfYearsOperatedRule");
    }

    final List<Rule> applicableRules =
        Stream.of(
                applicableNumberOfEmployeesRules,
                applicableCompanyTypeRules,
                applicableNumberOfYearsOperatedRules)
            .flatMap(Collection::stream)
            .collect(Collectors.toUnmodifiableList());
    snapshotService.snapshotRules(applicableRules);

    return applicableRules.stream()
        .reduce(
            0L,
            (aLong, rule) -> {
              if (!rule.test(scoringInput)) {
                return aLong;
              }

              return aLong + rule.getScore(scoringInput);
            },
            (aLong, aLong2) -> aLong);
  }
}
