Feature: Global authentication for all roles

  Scenario: Login all roles and cache tokens

# admin login
    Given url baseUrl
    And path '/auth/login'
    And request
"""
{
  "username": "admin",
  "password": "admin"
}
"""
    When method post
    Then status 200
    * def adminToken = response.accessToken


# librarian login
    Given url baseUrl
    And path '/auth/login'
    And request
"""
{
  "username": "librarian",
  "password": "librarian"
}
"""
    When method post
    Then status 200
    * def librarianToken = response.accessToken


# member login
    Given url baseUrl
    And path '/auth/login'
    And request
"""
{
  "username": "member",
  "password": "member"
}
"""
    When method post
    Then status 200
    * def memberToken = response.accessToken


# build token object
    * def tokens =
"""
{
  admin: "#(adminToken)",
  librarian: "#(librarianToken)",
  member: "#(memberToken)"
}
"""