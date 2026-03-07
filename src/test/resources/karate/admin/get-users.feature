Feature: Get users

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + adminToken

  Scenario: Admin gets all users

    Given path '/admin/users'
    When method get
    Then status 200
    And match response == '#[]'