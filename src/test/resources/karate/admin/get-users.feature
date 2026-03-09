Feature: Get users

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + adminToken

  Scenario: Admin gets all users

    Given path '/api/users'
    When method get
    Then status 200
    And match response == '#[]'