package sample;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jayway.jsonpath.JsonPath.read;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;

public class TestCase {


    @BeforeAll
    public static void checkingContentTypeAndStatusCode() {
        given().
                queryParam("appid", "47bdb22388de8f6e4b6dae4e5e7f60ab").
                queryParam("lat",55.75).
                queryParam("lon", 37.62).
                when().
                get("http://api.openweathermap.org/data/2.5/weather").
                then().
                assertThat().
                contentType(ContentType.JSON).statusCode(200);
    }

    @Test
    @DisplayName("╯°□°）╯ Тест на проверку того, что текущая температура меньше 300 градусов Кельвина")
    public void request_checkTemp() {
        String response = get("http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=47bdb22388de8f6e4b6dae4e5e7f60ab").
                asString();
        double weatherTemp = from(response).getDouble("main.temp");
        double anotherTemp = 300.00;
        assertTrue(weatherTemp < anotherTemp);
    }

    @Test
    @DisplayName("Тест для проверки корректности отдаваемых параметров, при передаче широты и долготы")
    public void request_checkLonLat() {
        String response = get("http://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=47bdb22388de8f6e4b6dae4e5e7f60ab").
                asString();
        double lon = read(response,"coord.lon");
        double lat = read(response,"coord.lat");
        String city = read(response,"name");
        String country = read(response,"sys.country");
        assertAll(
                () -> assertEquals(37.62, lon),
                () ->assertEquals(55.75, lat),
                () ->assertEquals("Moscow", city),
                () ->assertEquals("RU", country)
        );
    }

    //
    @Test
    @DisplayName("Тест для проверки что максимальная температура больше, чем минимальная")
    public void request_checkCel() {
        String response = get("http://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=47bdb22388de8f6e4b6dae4e5e7f60ab&units=metric").
                asString();
        double weatherTempMax = Double.parseDouble(read(response,"$.main.temp_max").toString());
        double weatherTempMin = Double.parseDouble(read(response,"$.main.temp_min").toString());
        assertTrue(weatherTempMax > weatherTempMin);
    }

    @Test
    @DisplayName("Тест для проверки, что температура через 16 часов будет больше 100 градусов Кельвина")
    public void request_checkTempAfter16h() {
        String response = get("http://api.openweathermap.org/data/2.5/onecall?lat=55.75&lon=37.62&exclude=current,minutely,daily&appid=47bdb22388de8f6e4b6dae4e5e7f60ab").
                asString();
        ArrayList temps = read(response, "$..temp");
        double t = Double.parseDouble(temps.get(15).toString());
        assertTrue(t > 100);
    }
}