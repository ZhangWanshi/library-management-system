Feature: Login API

  Background:
    * url baseUrl

  Scenario: Admin login

    Given path '/api/auth/login'
    And request
"""
{
  "username": "admin",
  "password": "admin"
}
"""
    When method post
    Then status 200

    And match response contains
"""
{
  accessToken: '#string',
  refreshToken: '#string',
  role: 'ADMIN'
}
"""