# Credit Service

## Assumptions

1. Based on the requirement sample, the credit service only outputs integer.
2. Based on the requirement sample, the credit service numeric inputs only allow integer.
3. Based on the requirement, the credit service should respond different error codes. Let me assume
   the response body is not restricted. I have defined a format to give a more specific error
   message.
4. Assume there is an external OAuth service, e.g. KeyCloak. This demo I am using Auth0 SAAS.

## Future

1. Extract ScoringRuleService Impl into another microservice, and invoke with Grpc.
2. Add one more property, `isExclusive` to Rule. This determines whether the rule can be applied only once.
3. To support `isExclusive`, we can modify the scoring service.
4. Add a persistent snapshot service, and update the service interface to snapshot more details.

## Project Structure

```
.
├── Dockerfile       -> Docker container definition. The http port is exposed as 8081.
├── README.md        -> Project README
├── mvnw             -> Maven wrapper for bash
├── mvnw.cmd         -> Maven wrapper for windows
├── pom.xml          -> Maven file
├── request.http     -> This contains the sample http requests
└── src
    ├── main
    │         ├── java
    │         │         └── info
    │         │             └── leochoi
    │         │                 └── creditservice
    │         │                     ├── CreditserviceApplication.java                        -> <entrypoint>
    │         │                     ├── ExceptionAdvice.java                                 -> Global exception handler
    │         │                     ├── HealthcheckController.java                           -> This controller provides the endpoint for healthcheck /healthz
    │         │                     ├── calculator
    │         │                     │         ├── CalculatorInput.java
    │         │                     │         ├── CalculatorOutput.java
    │         │                     │         └── controller
    │         │                     │             └── CalculatorController.java              -> This controller provides the endpoint TODO
    │         │                     ├── rule
    │         │                     │         ├── CompanyTypeRule.java
    │         │                     │         ├── NumberOfEmployeesRule.java
    │         │                     │         ├── NumberOfYearsOperatedRule.java
    │         │                     │         ├── Rule.java                                  -> The common interface of a Rule for scoring
    │         │                     │         ├── config
    │         │                     │         │         └── InMemoryRuleConfig.java          -> The definition of all rules
    │         │                     │         ├── controller
    │         │                     │         │         └── RuleController.java              -> This is for demostration 401 Authorized. /creditservice/v1/rule/reload
    │         │                     │         ├── exception
    │         │                     │         │         └── RuleConfigurationException.java
    │         │                     │         └── service
    │         │                     │             ├── InMemoryScoringRuleServiceImpl.java    -> This reads rules from InMemoryRuleConfig
    │         │                     │             └── ScoringRuleService.java                -> The interface to get rules for scroing
    │         │                     ├── scoring
    │         │                     │         ├── ScoringInput.java                          -> The internal input for ScoringService. Not sharing the same input with CalculatorInput.
    │         │                     │         ├── exception
    │         │                     │         │         └── NoRuleIsMatchedException.java
    │         │                     │         └── service
    │         │                     │             ├── AdditionScoringServiceImpl.java        -> This is the scoring service for applying all rules and produce the final result. Addition means `+`. 
    │         │                     │             └── ScoringService.java                    -> The interface is for scroing
    │         │                     ├── security
    │         │                     │   └── SecurityConfig.java                              -> This defines the basic OAuth2 config for all endpoints. By default, all endpoints are public. With @EnableGlobalMethodSecurity(prePostEnabled = true), each endpoint can be securied independently.
    │         │                     └── snapshot
    │         │                         ├── LoggingSnapshotServiceImpl.java
    │         │                         └── SnapshotService.java                             -> This is for snapshotting the applicable rules for calculation the score.
    │         └── resources
    │             ├── config.dev
    │             │         └── application.properties         -> The dev config
    │             ├── config.prod
    │             │         └── application.properties         -> The prod config
    │             └── config.uat
    │                       └── application.properties         -> The uat config
    └── test
        ├── java
        │         ├── CreditserviceIntegrationTests.java                         -> <entrypoint> Integration Tests with Cucumber
        │         └── info
        │             └── leochoi
        │                 └── creditservice
        │                     ├── CreditserviceApplicationTests.java             -> <entrypoint> Unit Tests with JUnit
        │                     ├── CreditserviceIntegrationTestsTestBase.java
        │                     ├── RestTemplateResponseErrorHandler.java          -> This avoids HttpClientErrorException
        │                     └── feature
        │                         └── CreditAssessmentCalculatorStepDefs.java    -> Cucumber test cases
        └── resources
            └── CreditAssessmentCalculator.feature

```

## Swagger

http://localhost:8081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

## Usage

1. Already set up the authorization server on Auth0. Here is hard-coded with the
   corresponding `client_id` and `client_secret` for demonstration. Suppose it is a s2s invocation,
   so `client_credentials` is being used to authorize.

```http request
POST https://my-creditservice.jp.auth0.com/oauth/token
Content-Type: application/json

{
  "audience": "http://localhost:8081",
  "grant_type": "client_credentials",
  "client_id": "CtTq2iK7Xl4CcBY7M4fm32R7Qfgmi7QY",
  "client_secret": "UBKpl46OSMrXCgoZpE88SE4dfqI2oCFmtSxia43DolRRpADE_h3leXBYtrqVfR84"
}
```

```json
{
  "access_token": "{{JWT}}",
  "expires_in": 86400,
  "token_type": "Bearer"
}
```

2. After getting the JWT from the 1. response, please request the endpoint
   `/creditservice/v1/calculator` with the `{{JWT}}`.

```http request
POST http://localhost:8081/creditservice/v1/calculator
Content-Type: application/json
Authorization: Bearer {{JWT}}

{
  "numberOfEmployees": 1,
  "companyType": "Others",
  "numberOfYearsOperated": 1
}
```

3. Without the JWT, the http request to `/creditservice/v1/calculator` is blocked, 403.

```http request
POST http://localhost:8081/creditservice/v1/calculator
Content-Type: application/json

{
  "numberOfEmployees": 1,
  "companyType": "Others",
  "numberOfYearsOperated": 1
}
```

4. Without the JWT, the http request to `/healthz` is allowed.

```http request
GET http://localhost:8081/healthz

```

5. Even with the JWT, the http request to `/creditservice/v1/rule/reload` is blocked. The generated
   jwt does not contain ROLE_MANAGEMENT, so the request is blocked, 401.

```http request
POST http://localhost:8081/creditservice/v1/rule/reload
Content-Type: application/json
Authorization: Bearer {{JWT}}

```