package info.leochoi.creditservice.scoring;

import org.jetbrains.annotations.NotNull;

public class ScoringInput {

  private final int numberOfEmployees;
  @NotNull private final String companyType;
  private final int numberOfYearsOperated;

  public ScoringInput(
      int numberOfEmployees, @NotNull String companyType, int numberOfYearsOperated) {
    this.numberOfEmployees = numberOfEmployees;
    this.companyType = companyType;
    this.numberOfYearsOperated = numberOfYearsOperated;
  }

  public int getNumberOfEmployees() {
    return numberOfEmployees;
  }

  @NotNull
  public String getCompanyType() {
    return companyType;
  }

  public int getNumberOfYearsOperated() {
    return numberOfYearsOperated;
  }

  @Override
  public String toString() {
    return String.format(
        "ScoringInput{numberOfEmployees=%d, companyType='%s', numberOfYearsOperated=%d}",
        numberOfEmployees, companyType, numberOfYearsOperated);
  }
}
