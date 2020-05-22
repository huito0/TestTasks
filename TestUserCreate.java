class TestUserCreate {
    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5/weather";
    }

    @Test
    final void testCreate() {
            given().
                    queryParam("appid", "token").
                    queryParam("q", "city").
                    queryParam("units", "metric").
                    log().all().
                    get().
              //  then().
                //    log().all().
                    // body("coord.lon", Matchers.equalTo(53.23f)).
                    // body("coord.lat", Matchers.equalTo(56.85f)).
                // statusCode(200);