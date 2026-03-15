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