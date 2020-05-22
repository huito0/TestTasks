package Test;

import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.builder.RequestSpecBuilder;

import java.util.ArrayList;

import static com.jayway.jsonpath.JsonPath.read;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BR {

    private static RequestSpecification requestSpec;
    private static RequestSpecification requestSpec2;


    @BeforeAll
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://api.openweathermap.org/data/2.5/weather").
                build();

        requestSpec2 = new RequestSpecBuilder().
                setBaseUri("https://api.openweathermap.org/data/2.5/onecall").
                build();
    }



    @Test // temperature check
    final void testCreate() {
        float temp = given().
                spec(requestSpec).
                queryParam("appid", "b415d80bdd2a677ffc4f828534ad4383").
                queryParam("q", "London").
                queryParam("units", "metric").
                when().
                get().
                then().
                extract().
                path("main.temp");
        assertTrue(temp < 450f);
    }



    @Test // coordinate check
    final void testCreate2() {

        String respJason = given().
                spec(requestSpec).
                queryParam("appid", "b415d80bdd2a677ffc4f828534ad4383").
                queryParam("q", "London").
                queryParam("units", "metric").
                when().
                get().
                asString();
        double lon = read(respJason, "coord.lon");
        double lat = read(respJason, "coord.lat");
        assertAll(
                () -> assertEquals(-0.13f, lon, 0.0002),
                () -> assertEquals(51.51f, lat, 0.0002)
        );

    }



    @Test // pressure check
    final void testCreate3() {
        int respJason = given().
                spec(requestSpec2).
                queryParam("appid", "b415d80bdd2a677ffc4f828534ad4383").
                queryParam("lat", "33.441792").
                queryParam("lon", "-94.037689").
                queryParam("exclude", "current,hourly,minutely").
                when().
                get().
                then().
                extract().
                path("daily.pressure[2]");
        assertTrue(respJason < 1200);




       // Float weather = read("daily.pressure[2]");
       // System.out.println(weather);

       //  float weather = response.jsonPath().get("daily.pressure[2]");
       // assertTrue(pressure > 1200, "it's cold now: " + pressure);


    }
}