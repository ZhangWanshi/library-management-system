Feature: Librarian views borrow records

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + librarianToken

  Scenario: Librarian retrieves all borrow records

    Given path '/api/borrowing/all-records'
    When method get
    Then status 200
    And match response == '#[]'