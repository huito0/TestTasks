package packagetest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static com.jayway.jsonpath.JsonPath.read;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    public String token = "ed0ef10ea541031713dfc12268cf9a45",
            qParamToken = "appid",
            qParamUnits = "units",
            unitsMetric = "metric", city = "Izhevsk";
    public String baseUri = "http://api.openweathermap.org/data/2.5/weather";
    public String baseUri1 = "https://api.openweathermap.org/data/2.5/onecall";


    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://api.openweathermap.org/data/2.5/weather").
                build();
    }

    @Test
    public void test1() {
        // Response response = request().g
        String json = given().
                baseUri(baseUri).
                queryParam(qParamToken, token).
                queryParam("q", city).
                queryParam(qParamUnits, unitsMetric).
                get().
                then().
                body("coord.lon", Matchers.equalTo(53.23f)).
                body("coord.lat", Matchers.equalTo(56.85f)).
                statusCode(200).
                        extract().jsonPath().prettify();
        System.out.println();
        System.out.println();
        //System.out.println(json);

        ArrayList weather = read(json, "$.weather[0].*");
        System.out.println(weather.get(1));

        String statusWeather = (String) weather.get(1);

        boolean weatherStatus = true;

        if (weather.get(1).equals("Clouds")) {
            weatherStatus = true;
        } else if (weather.get(1).equals("Rain")) {
            System.out.println("Возьми с собой зонт, на улице дождь!");
            // fail("Test not passed");
            weatherStatus = false;
        }

        Assertions.assertTrue(!weatherStatus == false, "the weather is nasty");
    }

    @Test
    public void test2() {
        int i = 200;
        RestAssured.given().
                baseUri(baseUri).
                queryParam(qParamToken, token).
                queryParam("q", city).
                queryParam(qParamUnits, unitsMetric).
                get().
        then().
                header("Content-Type", Matchers.startsWithIgnoringCase("application/json")).
                header("X-Cache-Key", Matchers.endsWithIgnoringCase("units=metric")).
        statusCode(i);
    }

    @Test
    public void test3() {
        String jsonResponse = RestAssured.given().baseUri(baseUri1).
                queryParam("lat", 33.441792).
                queryParam("lon", -94.037689).
                queryParam("exclude", "hourly").
                queryParam(qParamToken, token).
                //queryParam("q", city).
                queryParam(qParamUnits, unitsMetric).
                get().
                then().extract().jsonPath().prettify();

        ArrayList response = read(jsonResponse, "$.minutely[*].dt");
        Integer length = response.size();
        System.out.println(length);
        Assertions.assertEquals(61, length);

    }

    }
