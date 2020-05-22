package com.ichisora;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class TestWeather {
    static Response response = null;
    static int pressure = 0;
    static float temp;
    static JsonPath jsonPath;
    static String mainWeather;
    static float windSpeed;

    @BeforeAll
    public static void weather() {
        try {
            response = given().
                    when().
                    get("https://api.openweathermap.org/data/2.5/onecall?lat=56.84976&lon=53.20448&appid=48493637dedf521c6fb9e449aa973020");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        assert response != null;
        jsonPath = response.jsonPath();
    }

    @Test
    public void checkCurrentPressure() {
        pressure = jsonPath.get("current.pressure");
        assertTrue(pressure < 1020, "Давление слишком высокое");
    }

    @Test
    public void checkTomorrowRain() {
        mainWeather = jsonPath.get("daily[1].weather[0].main");
        assertTrue(mainWeather.equals("Rain"), "Должен идти дождь");
    }

    @Test
    public void checkTempAfterTwoHours() {
        temp = jsonPath.get("hourly[2].temp");
        int t = 10;
        assertTrue((temp - 273.15f) > t, "Температура меньше " + t + "°C");
    }

    @ParameterizedTest(name = "{index}=> {0}")
    @ValueSource(ints = {1, 4, 5, 7})
    public void checkWindSpeed(int num) {
        windSpeed = jsonPath.get("daily[" + num + "].wind_speed");
        assertTrue(windSpeed < 9.9f, "Сильный ветер");
    }
}
