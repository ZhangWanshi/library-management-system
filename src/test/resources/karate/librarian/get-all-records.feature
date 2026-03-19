Feature: Librarian views borrow records

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + librarianToken

  Scenario: Librarian retrieves all borrow records

    Given path '/api/borrowing/all-records'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Unauthorized member tries to view all borrow records
    Given path '/api/borrowing/all-records'
    And header Authorization = 'Bearer ' + memberToken
    When method get
    Then status 403