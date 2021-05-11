package info.leochoi.creditservice.calculator.controller;

import info.leochoi.creditservice.calculator.CalculatorInput;
import info.leochoi.creditservice.calculator.CalculatorOutput;
import info.leochoi.creditservice.scoring.ScoringInput;
import info.leochoi.creditservice.scoring.service.ScoringService;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

  private final ScoringService scoringService;

  public CalculatorController(ScoringService scoringService) {
    this.scoringService = scoringService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(
      path = "/creditservice/v1/calculator",
      consumes = "application/json",
      produces = "application/json")
  public CalculatorOutput calculator(@RequestBody @Valid CalculatorInput calculatorInput) {
    final ScoringInput scoringInput =
        new ScoringInput(
            calculatorInput.getNumberOfEmployees(),
            calculatorInput.getCompanyType(),
            calculatorInput.getNumberOfYearsOperated());

    final long creditScore = scoringService.calculateScore(scoringInput);

    final CalculatorOutput calculatorOutput = new CalculatorOutput();
    calculatorOutput.setCreditScore(creditScore);

    return calculatorOutput;
  }
}
