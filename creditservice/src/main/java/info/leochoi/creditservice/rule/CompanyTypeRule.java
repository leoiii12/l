package info.leochoi.creditservice.rule;

import info.leochoi.creditservice.scoring.ScoringInput;
import org.jetbrains.annotations.NotNull;

public class CompanyTypeRule implements Rule {

  private final @NotNull String companyType;
  private final int score;

  public CompanyTypeRule(final @NotNull String companyType, final int score) {
    this.companyType = companyType;
    this.score = score;
  }

  @Override
  public boolean test(final @NotNull ScoringInput scoringInput) {
    return scoringInput.getCompanyType().equals(companyType);
  }

  @Override
  public int getScore(final @NotNull ScoringInput scoringInput) {
    return score;
  }

  @Override
  public String toString() {
    return String.format("CompanyTypeRule{companyType='%s', score=%d}", companyType, score);
  }
}
