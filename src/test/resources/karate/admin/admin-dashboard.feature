Feature: Admin views borrowing statistics

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + adminToken

  Scenario: Admin gets summary stats

    Given path '/api/books/summary'
    When method get
    Then status 200

    And match response contains
    """
    {
      totalBooks: '#number',
      totalBorrowRecords: '#number',
      totalMembers: '#number'
    }
    """


  Scenario: Admin gets books by category

    Given path '/api/books/by-category'
    When method get
    Then status 200
    And match response == '#[]'


  Scenario: Admin gets most borrowed books

    Given path '/api/borrowing/most-borrowed'
    When method get
    Then status 200
    And match response == '#[]'