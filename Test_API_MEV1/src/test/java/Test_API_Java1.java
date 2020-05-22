import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testng.annotations.Parameters;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runner.Runner;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(Parameterized.class)
public class Test_API_Java1 {
    public static String[] ciry = {"London", "Liverpool", "Riga", "Belfast", "Elgin", "Bude", "Ashford"};
    @Parameters
    public static Collection<Object[]> Codes()  {
        return  Arrays.asList(new Object[][]
                {
                        { "q=",ciry[0], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[1], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[2], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[3], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[4], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[5], "APPID=9e36d5af654c0bc9e5994799dd93bd19" },
                        { "q=",ciry[6], "appid=9e36d5af654c0bc9e5994799dd93bd19" }
                });

    }

     @ParameterizedTest
     @MethodSource("Codes")
     @DisplayName("Проверка на соответствие ответа формату json, соответствие статус кода, соответствие города в запросе и в ответе")
     public void requestUs(String payload1, String payload2, String payload3) {

        given().
                pathParam("payload1", payload1).
                pathParam("payload2", payload2).
                pathParam("payload3", payload3).
                contentType(ContentType.JSON).
                log().all().
                when().
                get("http://api.openweathermap.org/data/2.5/weather?{payload1}{payload2}&{payload3}").
                then().
                log().all().
                assertThat().
                statusCode(200).
                contentType("application/json").
                body("name", equalTo(payload2));

    }


    @ParameterizedTest
    @MethodSource("Codes")
    @DisplayName("Проверка на соответствие города стране")
    public void requestUs1(String payload1, String payload2, String payload3) {

        given().
                pathParam("payload1", payload1).
                pathParam("payload2", payload2).
                pathParam("payload3", payload3).
                contentType(ContentType.JSON).
                when().
                get("http://api.openweathermap.org/data/2.5/weather?{payload1}{payload2}&{payload3}").
                then().
                assertThat().
                body("sys.'country'", equalTo("GB"));
    }


    @ParameterizedTest
    @MethodSource("Codes")
    @DisplayName("Проверка на соответствие количества полей в разделе погода")
    public void requestUs2(String payload1, String payload2, String payload3) {

        given().
                pathParam("payload1", payload1).
                pathParam("payload2", payload2).
                pathParam("payload3", payload3).
                contentType(ContentType.JSON).
                when().
                get("http://api.openweathermap.org/data/2.5/weather?{payload1}{payload2}&{payload3}").
                then().
                assertThat().
                body("weather[0].size()", equalTo(4));
    }


    @ParameterizedTest
    @MethodSource("Codes")
    @DisplayName("Проверка на то что температура в пределах максимума и минимума")
    public void requestUs3(String payload1, String payload2, String payload3) {

        String j = "http://api.openweathermap.org/data/2.5/weather?";

        j = j.concat(payload1);
        j = j.concat(payload2);
        j = j.concat("&");
        j = j.concat(payload3);

        Response json = get(j);
        Float temp = json.path("main.'temp'");
        Float temp1 = json.path("main.'feels_like'");
        Float temp2 = json.path("main.'temp_max'");
        System.out.println(temp+ " " +temp1+ " " +temp2);
        assertAll(() -> assertTrue(temp != temp1),
                () -> assertTrue(temp <= temp2));

        given().
                pathParam("payload1", payload1).
                pathParam("payload2", payload2).
                pathParam("payload3", payload3).
                contentType(ContentType.JSON).
                when().
                get("http://api.openweathermap.org/data/2.5/weather?{payload1}{payload2}&{payload3}").
                then().
                assertThat().
                body("cod",equalTo(200));
    }


    @ParameterizedTest
    @MethodSource("Codes")
    @DisplayName("Проверка на то что температура в пределах максимума и минимума")
    public void requestUs4(String payload1, String payload2, String payload3) {

        Response response =
                given().
                        pathParam("payload1", payload1).
                        pathParam("payload2", payload2).
                        pathParam("payload3", payload3).
                        when().
                        get("http://api.openweathermap.org/data/2.5/weather?{payload1}{payload2}&{payload3}").
                        then().
                        contentType(ContentType.JSON).
                        body("cod",equalTo(200)).
                        extract().
                        //path("main");
                                response();

        Float temp = response.path("main.'temp'");
        Float temp1 = response.path("main.'temp_min'");
        Float temp2 = response.path("main.'temp_max'");
        System.out.println(temp+ " " +temp1+ " " +temp2);
        assertAll(() -> assertTrue(temp >= temp1),
                () -> assertTrue(temp <= temp2));

    }
}