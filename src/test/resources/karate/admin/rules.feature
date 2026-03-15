Feature: Admin manages borrowing rules

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + adminToken

  Scenario: Admin updates borrowing rules

    Given path '/api/rules'
    And request
    """
    {
      "maxBooksAllowed": 3,
      "borrowDurationDays": 14
    }
    """
    When method put
    Then status 200


  Scenario: Admin retrieves borrowing rules

    Given path '/api/rules'
    When method get
    Then status 200
    And match response contains
    """
    {
      maxBooksAllowed: '#number',
      borrowDurationDays: '#number'
    }
    """