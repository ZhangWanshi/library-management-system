Feature: Member returns a borrowed book

  Background:
    * url baseUrl

  Scenario: Member returns book

# librarian creates book
    Given path '/api/books'
    And header Authorization = 'Bearer ' + librarianToken
    And request
    """
    {
      "title": "Java Concurrency",
      "author": "Brian Goetz",
      "isbn": "9780321349606",
      "category": "Programming"
    }
    """
    When method post
    Then status 201

    * def bookId = response.id


# member borrows book
    Given path '/api/borrowing/' + bookId
    And header Authorization = 'Bearer ' + memberToken
    When method post
    Then status 200


# get borrow records
    Given path '/api/borrowing/my-records'
    And header Authorization = 'Bearer ' + memberToken
    When method get
    Then status 200

    * def recordId = response[0].id


# return book
    Given path '/api/borrowing/return/' + recordId
    And header Authorization = 'Bearer ' + memberToken
    When method post
    Then status 200