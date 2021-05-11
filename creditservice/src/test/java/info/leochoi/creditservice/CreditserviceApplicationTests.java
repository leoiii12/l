package info.leochoi.creditservice;

import static org.assertj.core.api.Assertions.assertThat;

import info.leochoi.creditservice.calculator.controller.CalculatorController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class CreditserviceApplicationTests {
  @Autowired private CalculatorController calculatorController;
  @Autowired private HealthcheckController healthcheckController;

  @Test
  void contextLoads() {
    assertThat(calculatorController).isNotNull();
    assertThat(healthcheckController).isNotNull();
  }
}
