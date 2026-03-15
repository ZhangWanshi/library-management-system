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

  Scenario: Borrowing limit is zero
    Given path '/api/rules'
    And request
    """
    {
      "maxBooksAllowed": -1,
      "borrowDurationDays": 7
    }
    """
    When method put
    Then status 400

  Scenario: Borrow duration negative
    Given path '/api/rules'
    And request
    """
    {
      "maxBooksAllowed": 5,
      "borrowDurationDays": -1
    }
    """
    When method put
    Then status 400

  Scenario: Unauthorized user tries to update rules
    Given path '/api/rules'
    And request
    """
    {
      "maxBooksAllowed": 5,
      "borrowDurationDays": 7
    }
    """
    And header Authorization = 'Bearer ' + memberToken
    When method put
    Then status 403