Feature: Librarian adds books

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + librarianToken

  Scenario: Librarian creates new book

    Given path '/api/books'
    And request
    """
    {
      "title": "Clean Code",
      "author": "Robert Martin",
      "isbn": "9780132350884",
      "category": "Programming"
    }
    """
    When method post
    Then status 201

    And match response.title == 'Clean Code'
    And match response.status == 'AVAILABLE'

  Scenario: Missing title
    Given path '/api/books'
    And request
    """
    {
      "author": "Author A",
      "isbn": "12345",
      "category": "Programming"
    }
    """
    When method post
    Then status 400

  Scenario: Unauthorized user tries to add book
    Given path '/api/books'
    And request
    """
    {
      "title": "Book Title",
      "author": "Author A",
      "isbn": "12345",
      "category": "Programming"
    }
    """
    And header Authorization = 'Bearer ' + memberToken
    When method post
    Then status 403