Feature: Credit Assessment Calculator
  This is a simple credit assessment calculator feature

  Scenario Outline: Calculate Credit Assessment Score
    When The calculateCreditAssessmentScore API is called with <numberOfEmployees>, <companyType>, <numberOfYearsOperated> parameters
    Then The credit assessment score should match <creditScore>

    Examples:
      | numberOfEmployees | companyType                 | numberOfYearsOperated | creditScore |
      | 6                 | "Sole Proprietorship"       | 5                     | 7           |
      | 10                | "Limited Liability Company" | 8                     | 13          |
      | 999999            | "Limited Liability Company" | 999999                | 31          |

      | 1                 | "Others"                    | 0                     | 2           |
      | 2                 | "Others"                    | 0                     | 3           |
      | 5                 | "Others"                    | 0                     | 3           |
      | 6                 | "Others"                    | 0                     | 4           |
      | 10                | "Others"                    | 0                     | 4           |
      | 11                | "Others"                    | 0                     | 6           |
      | 50                | "Others"                    | 0                     | 6           |
      | 51                | "Others"                    | 0                     | 9           |
      | 200               | "Others"                    | 0                     | 9           |
      | 201               | "Others"                    | 0                     | 14          |
      | 10000             | "Others"                    | 0                     | 14          |

      | 1                 | "Sole Proprietorship"       | 0                     | 3           |
      | 1                 | "Partnership"               | 0                     | 5           |
      | 1                 | "Limited Liability Company" | 0                     | 7           |
      | 1                 | "Others"                    | 0                     | 2           |

      | 1                 | "Others"                    | 0                     | 2           |
      | 1                 | "Others"                    | 1                     | 2           |
      | 1                 | "Others"                    | 2                     | 3           |
      | 1                 | "Others"                    | 3                     | 3           |
      | 1                 | "Others"                    | 4                     | 4           |
      | 1                 | "Others"                    | 6                     | 4           |
      | 1                 | "Others"                    | 7                     | 6           |
      | 1                 | "Others"                    | 10                    | 6           |
      | 1                 | "Others"                    | 11                    | 14          |
      | 1                 | "Others"                    | 1000                  | 14          |

  Scenario Outline: Calculate Credit Assessment Score with Invalid Inputs
    When The calculateCreditAssessmentScore API is called with invalid <numberOfEmployees>, <companyType>, <numberOfYearsOperated> parameters
    Then The credit assessment score should throw 400

    Examples:
      | numberOfEmployees | companyType                 | numberOfYearsOperated |
      | -1                | "Sole Proprietorship"       | 5                     |
      | 6                 | "ABC"                       | 5                     |
      | 10                | "Limited Liability Company" | -1                    |

  Scenario Outline: Calculate Credit Assessment Score without authentication
    When The calculateCreditAssessmentScore API is called without authentication
    Then The credit assessment score should throw 403

    Examples:
      | i |
      | 1 |