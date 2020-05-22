package mypackage;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.jayway.jsonpath.JsonPath.read;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCase {
    private static RequestSpecification requestMyWeather, requestMyOneCall;
    private static ResponseSpecification statusCodeTrue;

    double latMy = 33.44;
    double lonMy = -94.04;

    @BeforeAll
    public static void getRequestWeather() {
        requestMyWeather = new RequestSpecBuilder().
                setBaseUri("http://api.openweathermap.org/data/2.5/weather").
                build();

        requestMyOneCall = new RequestSpecBuilder().
                setBaseUri("https://api.openweathermap.org/data/2.5/onecall").
                build();

    }
    @BeforeAll
    public static void getStatusCode() {
        statusCodeTrue = new ResponseSpecBuilder().
                expectStatusCode(200).
                build();
    }


    @Test //Сравниваем температуру на данный момент с определенным значением
    public void myTestCurrentTemp() {

        String currentAll = given().
                spec(requestMyWeather).
                queryParam("appid", "faba8389341527d0387cdcfc77b2969a").
                queryParam("q", "London").
                queryParam("units", "metric").
                get().
                then().
                spec(statusCodeTrue).
                extract().jsonPath().prettify();
        double currentTemp = read(currentAll, "main.temp");
        double tempDef = 30.00f;
        assertTrue(currentTemp < tempDef);
    }

    @Test //Сравнивем давления через час с определенным значением
    public void myTestPressureAfterHour() {
        String hourlyAll = given().
                spec(requestMyOneCall).
                queryParam("appid", "faba8389341527d0387cdcfc77b2969a").
                queryParam("lat", 33.44179).
                queryParam("lon", -94.037689).
                queryParam("units", "metric").
                queryParam("exclude", "current,daily,minutely").
                get().then().spec(statusCodeTrue).extract().jsonPath().prettify();
        int pressureHourly = read(hourlyAll, "hourly[0].pressure");
        //float aga = Float.parseFloat(hourlyPressure);
        int myPressure = 1000;
        assertTrue(pressureHourly > myPressure);
    }

    @Test //Проверка на то, что координаты переданные в параметрах совпадают с координатами в JSON ответе и не принимают значения друг друга
    public void myTestLonLatTrue() {
        String fullJson = given().
                spec(requestMyOneCall).
                queryParam("appid", "faba8389341527d0387cdcfc77b2969a").
                queryParam("lat", latMy).
                queryParam("lon", lonMy).
                queryParam("units", "metric").
                queryParam("exclude", "current,daily,minutely").
                get().then().spec(statusCodeTrue).extract().jsonPath().prettify();
        double latFact = read(fullJson, "lat");
        double lonFact = read(fullJson, "lon");
        assertAll(
                () -> assertTrue(lonFact == lonMy),
                () -> assertTrue(latFact == latMy)
        );
    }

    @Test //Проверка на то, что значения температуры приходят именно в градусах
    public void myTestExpectRain() {
        String myJson = given().
                spec(requestMyOneCall).
                queryParam("appid", "faba8389341527d0387cdcfc77b2969a").
                queryParam("lat", latMy).
                queryParam("lon", lonMy).
                queryParam("exclude", "current,daily,minutely").
                queryParam("units", "metric").
                get().then().spec(statusCodeTrue).extract().jsonPath().prettify();
        String testRain = read(myJson, "hourly[0].weather[0].main");
        String testRainExp = "Rain";
        assertEquals(testRain, testRainExp);

    }
}
