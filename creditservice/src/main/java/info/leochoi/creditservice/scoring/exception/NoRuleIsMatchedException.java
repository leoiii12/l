package info.leochoi.creditservice.scoring.exception;

public class NoRuleIsMatchedException extends RuntimeException {
  private final String ruleType;

  public NoRuleIsMatchedException(String ruleType) {
    super();

    this.ruleType = ruleType;
  }

  public String getRuleType() {
    return ruleType;
  }
}
