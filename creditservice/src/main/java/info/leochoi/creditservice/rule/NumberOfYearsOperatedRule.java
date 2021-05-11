package info.leochoi.creditservice.rule;

import info.leochoi.creditservice.rule.exception.RuleConfigurationException;
import info.leochoi.creditservice.scoring.ScoringInput;
import org.jetbrains.annotations.NotNull;

public class NumberOfYearsOperatedRule implements Rule {

  private final int min;
  private final int max;
  private final int score;

  public NumberOfYearsOperatedRule(final int min, final int max, final int score) {
    this.min = min;
    this.max = max;
    this.score = score;

    if (min > max) {
      throw new RuleConfigurationException(
          "NumberOfYearsOperatedRule",
          String.format("The max=[%d] should be greater than the min=[%d].", max, min));
    }
  }

  @Override
  public boolean test(final @NotNull ScoringInput scoringInput) {
    return min <= scoringInput.getNumberOfYearsOperated()
        && scoringInput.getNumberOfYearsOperated() <= max;
  }

  @Override
  public int getScore(final @NotNull ScoringInput scoringInput) {
    return score;
  }

  @Override
  public String toString() {
    return String.format("NumberOfYearsOperatedRule{min=%d, max=%d, score=%d}", min, max, score);
  }
}
