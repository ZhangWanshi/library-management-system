Feature: Member borrows a book

  Background:
    * url baseUrl

  Scenario: Member borrows available book

# librarian creates book
    Given path '/api/books'
    And header Authorization = 'Bearer ' + librarianToken
    And request
    """
    {
      "title": "Spring Boot Guide",
      "author": "John Doe",
      "isbn": "1234567890",
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

  Scenario: Book already borrowed
    * def bookId = 1
    Given path '/api/borrowing/' + bookId
    And header Authorization = 'Bearer ' + memberToken
    When method post
    Then status 400

  Scenario: Borrow limit exceeded
    * def bookId = 2
    Given path '/api/borrowing/' + bookId
    And header Authorization = 'Bearer ' + memberToken
    When method post
    Then status 400