package info.leochoi.creditservice.calculator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CalculatorInput {

  @NotNull
  @Min(1)
  @Max(Integer.MAX_VALUE)
  private int numberOfEmployees;

  @NotBlank
  @Size(min = 1, max = 512)
  private String companyType;

  @NotNull
  @Min(0)
  @Max(Integer.MAX_VALUE)
  private int numberOfYearsOperated;

  public int getNumberOfEmployees() {
    return numberOfEmployees;
  }

  public void setNumberOfEmployees(int numberOfEmployees) {
    this.numberOfEmployees = numberOfEmployees;
  }

  public String getCompanyType() {
    return companyType;
  }

  public void setCompanyType(String companyType) {
    this.companyType = companyType;
  }

  public int getNumberOfYearsOperated() {
    return numberOfYearsOperated;
  }

  public void setNumberOfYearsOperated(int numberOfYearsOperated) {
    this.numberOfYearsOperated = numberOfYearsOperated;
  }
}
