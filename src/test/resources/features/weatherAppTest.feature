Feature: Weather Application Test

#  Scenario: Verify the displays current weather forcast including temperature and conditions
#      Given I am a general user of the weather forcast
#      When I request the current weather
#      Then The weather app displays the current weather forcast including temperature and condition
#
#  Scenario: Verify the weather forcasts for each hour including temperature and conditions.
#    Given I am a general user of the weather forcast
#    When I request to view hourly weather forcasts for the day
#    Then The weather app provides me with weather forcasts for each hour including temperature and conditions

  Scenario: Verify the weather app displays the weather forcast for the entire week.
    Given I am a general user of the weather forcast
    When I Select to view weekly weather forcast
    Then The weather app displays the weather forcast for the entire week