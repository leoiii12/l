package info.leochoi.creditservice.rule;

import info.leochoi.creditservice.scoring.ScoringInput;
import org.jetbrains.annotations.NotNull;

public interface Rule {

  /**
   * A method to test whether this rule applies
   *
   * @return whether the rule applies
   */
  boolean test(final @NotNull ScoringInput scoringInput);

  /**
   * A method to get the corresponding score of the rule
   *
   * @return the corresponding score
   */
  int getScore(final @NotNull ScoringInput scoringInput);
}
