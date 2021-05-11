package info.leochoi.creditservice.rule.exception;

import org.jetbrains.annotations.NotNull;

public class RuleConfigurationException extends RuntimeException {
  private final String ruleName;

  public RuleConfigurationException(@NotNull String ruleName, @NotNull String msg) {
    super(msg);

    this.ruleName = ruleName;
  }

  public String getRuleName() {
    return ruleName;
  }
}
