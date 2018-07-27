Feature: The timeseries validation for negative values flags records as expected

  Scenario: A positive value is not flagged
    Given an ADO with the following timeseries in CONSOLIDATION_C0
      | DATE     | TIME   | CLOSE   |
      | 20180702 | 235959 | 100 [1] |
    When we apply the validation function non-positive and revalidate
    Then we get the following timeseries
      | DATE     | TIME   | CLOSE   |
      | 20180702 | 235959 | 100 [1] |

  Scenario: A negative value is flagged
    Given an ADO with the following timeseries in CONSOLIDATION_C0
      | DATE     | TIME   | CLOSE   |
      | 20180702 | 235959 | -100 [1] |
    When we apply the validation function non-positive and revalidate
    Then we get the following timeseries
      | DATE     | TIME   | CLOSE   |
      | 20180702 | 235959 | -100 [130] |
