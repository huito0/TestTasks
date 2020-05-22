package org.openweathermap.test;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static com.jayway.jsonpath.JsonPath.read;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class First_API_Tests {
    @Test
    public void ID_city() {
        given().
                when().
                get("https://api.openweathermap.org/data/2.5/weather?q=Neuendettelsau&appid=6e1192b97f6f376cdc342594a71a7310").
                then()
                .assertThat()
                .body("id", equalTo(2865798));
    }
    @Test
    public void CodeStatus() {
        given().
                when().
                get("https://api.openweathermap.org/data/2.5/weather?q=Cairns").
                then().
                statusCode(401)
                .and()
                .assertThat()
                .body("cod", equalTo(401));
    }
    @Test
    public void Check_temp_min() {
        String expr = get ("https://api.openweathermap.org/data/2.5/forecast?q=London&appid=6e1192b97f6f376cdc342594a71a7310").asString();
        double temp_min = read(expr, "$.list[4].main.temp_min");
        assertTrue(temp_min >= 255);
    }
    @Test
    public void Check_city() {
        String expr = get ("https://api.openweathermap.org/data/2.5/weather?lon=53.23&lat=56.85&appid=6e1192b97f6f376cdc342594a71a7310").asString();
        String name_city = read(expr, "$.name");
        int id_city = read(expr, "$.id");
        assertAll(
                () -> assertEquals("Izhevsk",name_city),
                () -> assertEquals(554840,id_city)
        );
    }
    @Test
    public void Check_lang() {
        String expr = get ("https://api.openweathermap.org/data/2.5/weather?q=Izhevsk&lang=ru&appid=6e1192b97f6f376cdc342594a71a7310").asString();
        String lang = read(expr,"$.name");
        assertEquals("Ижевск",lang);
    }
}
