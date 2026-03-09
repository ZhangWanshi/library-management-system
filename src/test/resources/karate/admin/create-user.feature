Feature: Create user

  Background:
    * url baseUrl
    * header Authorization = 'Bearer ' + adminToken

  Scenario: Admin creates new user

    Given path '/api/users'
    And request
    """
    {
      "username": "member2",
      "password": "1234",
      "email": "member2@test.com",
      "role": "MEMBER"
    }
    """
    When method post
    Then status 201

    And match response.username == 'member2'