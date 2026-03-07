Feature: Refresh token API

  Background:
    * url baseUrl

  Scenario: Refresh access token using refresh token

# step1 login first
    Given path '/auth/login'
    And request
"""
{
  "username": "admin",
  "password": "admin"
}
"""
    When method post
    Then status 200

    * def refreshToken = response.refreshToken

# step2 call refresh API
    Given path '/auth/refresh'
    And request
"""
{
  "refreshToken": "#(refreshToken)"
}
"""
    When method post
    Then status 200

    And match response contains
"""
{
  accessToken: '#string'
}
"""