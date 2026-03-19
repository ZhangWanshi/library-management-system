Feature: Member returns a borrowed book

  Background:
    * url baseUrl

  Scenario: Member returns book


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