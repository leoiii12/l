package info.leochoi.creditservice.rule.config;

import info.leochoi.creditservice.rule.CompanyTypeRule;
import info.leochoi.creditservice.rule.NumberOfEmployeesRule;
import info.leochoi.creditservice.rule.NumberOfYearsOperatedRule;
import info.leochoi.creditservice.rule.Rule;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryRuleConfig {

  @Qualifier(value = "inMemoryRules")
  @Bean
  public List<Rule> inMemoryRules() {
    return Arrays.asList(
        /* NumberOfEmployeesRules */
        new NumberOfEmployeesRule(1, 1, 1),
        new NumberOfEmployeesRule(2, 5, 2),
        new NumberOfEmployeesRule(6, 10, 3),
        new NumberOfEmployeesRule(11, 50, 5),
        new NumberOfEmployeesRule(51, 200, 8),
        new NumberOfEmployeesRule(201, Integer.MAX_VALUE, 13),

        /* CompanyTypeRules */
        new CompanyTypeRule("Sole Proprietorship", 1),
        new CompanyTypeRule("Partnership", 3),
        new CompanyTypeRule("Limited Liability Company", 5),
        new CompanyTypeRule("Others", 0),

        /* NumberOfYearsOperatedRules */
        new NumberOfYearsOperatedRule(0, 1, 1),
        new NumberOfYearsOperatedRule(2, 3, 2),
        new NumberOfYearsOperatedRule(4, 6, 3),
        new NumberOfYearsOperatedRule(7, 10, 5),
        new NumberOfYearsOperatedRule(11, Integer.MAX_VALUE, 13));
  }
}
