package info.leochoi.creditservice.feature;

import static org.assertj.core.api.Assertions.assertThat;

import info.leochoi.creditservice.CreditserviceIntegrationTestsTestBase;
import info.leochoi.creditservice.RestTemplateResponseErrorHandler;
import info.leochoi.creditservice.calculator.CalculatorInput;
import info.leochoi.creditservice.calculator.CalculatorOutput;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CreditAssessmentCalculatorStepDefs extends CreditserviceIntegrationTestsTestBase {
  @LocalServerPort private int port;

  private ResponseEntity<CalculatorOutput> lastCalculatorOutputResponseEntity;

  private String calculatorUrl() {
    String SERVER_URL = "http://localhost";
    return SERVER_URL + ":" + port + "/creditservice/v1/calculator";
  }

  @When("The calculateCreditAssessmentScore API is called with {int}, {string}, {int} parameters")
  public void theCalculateCreditAssessmentScoreApiIsCalledWithParameters(
      Integer numberOfEmployees, String companyType, Integer numberOfYearsOperated) {
    final CalculatorInput calculatorInput = new CalculatorInput();
    calculatorInput.setNumberOfEmployees(numberOfEmployees);
    calculatorInput.setCompanyType(companyType);
    calculatorInput.setNumberOfYearsOperated(numberOfYearsOperated);

    final AuthTokenOutput authTokenOutput =
        new RestTemplate()
            .postForEntity(
                "https://my-creditservice.jp.auth0.com/oauth/token",
                new AuthTokenInput(),
                AuthTokenOutput.class)
            .getBody();

    lastCalculatorOutputResponseEntity = getCalculatorOutput(calculatorInput, authTokenOutput);
  }

  @Then("The credit assessment score should match {long}")
  public void theCreditAssessmentScoreShouldMatch(Long creditScore) {
    assertThat(lastCalculatorOutputResponseEntity.getBody().getCreditScore())
        .isEqualTo(creditScore);
  }

  @When(
      "The calculateCreditAssessmentScore API is called with invalid {int}, {string}, {int} parameters")
  public void theCalculateCreditAssessmentScoreAPIIsCalledWithInvalidParameters(
      Integer numberOfEmployees, String companyType, Integer numberOfYearsOperated) {
    final CalculatorInput calculatorInput = new CalculatorInput();
    calculatorInput.setNumberOfEmployees(numberOfEmployees);
    calculatorInput.setCompanyType(companyType);
    calculatorInput.setNumberOfYearsOperated(numberOfYearsOperated);

    final AuthTokenOutput authTokenOutput =
        new RestTemplate()
            .postForEntity(
                "https://my-creditservice.jp.auth0.com/oauth/token",
                new AuthTokenInput(),
                AuthTokenOutput.class)
            .getBody();

    lastCalculatorOutputResponseEntity = getCalculatorOutput(calculatorInput, authTokenOutput);
  }

  @Then("The credit assessment score should throw 400")
  public void theCreditAssessmentScoreShouldThrow400() {
    assertThat(lastCalculatorOutputResponseEntity.getStatusCode())
        .isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @When("The calculateCreditAssessmentScore API is called without authentication")
  public void theCalculateCreditAssessmentScoreAPIIsCalledWithoutAuthentication() {
    final CalculatorInput calculatorInput = new CalculatorInput();
    calculatorInput.setNumberOfEmployees(1);
    calculatorInput.setCompanyType("Others");
    calculatorInput.setNumberOfYearsOperated(1);

    lastCalculatorOutputResponseEntity = getCalculatorOutput(calculatorInput, null);
  }

  @Then("The credit assessment score should throw 403")
  public void theCreditAssessmentScoreShouldThrow403() {
    assertThat(lastCalculatorOutputResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  private ResponseEntity<CalculatorOutput> getCalculatorOutput(
      @NotNull CalculatorInput calculatorInput, @Nullable AuthTokenOutput authTokenOutput) {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setOutputStreaming(false);
    restTemplate.setRequestFactory(requestFactory);

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    if (authTokenOutput != null) {
      httpHeaders.setBearerAuth(authTokenOutput.access_token);
    }

    final HttpEntity<CalculatorInput> httpEntity = new HttpEntity<>(calculatorInput, httpHeaders);

    return restTemplate.exchange(
        calculatorUrl(), HttpMethod.POST, httpEntity, CalculatorOutput.class);
  }

  public static class AuthTokenInput {
    public String audience = "http://localhost:8081";
    public String grant_type = "client_credentials";
    public String client_id = "CtTq2iK7Xl4CcBY7M4fm32R7Qfgmi7QY";
    public String client_secret =
        "UBKpl46OSMrXCgoZpE88SE4dfqI2oCFmtSxia43DolRRpADE_h3leXBYtrqVfR84";
  }

  public static class AuthTokenOutput {
    public String access_token;
    public String expires_in;
    public String token_type;
  }
}
