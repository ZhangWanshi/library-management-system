Feature: Member views books

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + memberToken

  Scenario: Member retrieves book list

    Given path '/api/books'
    When method get
    Then status 200
    And match response == '#[]'