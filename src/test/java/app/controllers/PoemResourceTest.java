package app.controllers;

import app.entities.Poem;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

public class PoemResourceTest
{

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupAll()
    {
        ApplicationConfig
            .getInstance()
            .initiateServer()
            .setRoute(Routes.getRoutes())
            .startServer(7777);
    }

    @BeforeEach
    void setUp()
    {
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    @DisplayName("Test for getting poem by id")
    public void getByIdTest()
    {
        given()
            .when()
            .get("/api/poem/1")
            .then()
            .statusCode(200)
            .body("id", equalTo(2));
    }

    @Test
    @DisplayName("Test for POST Request of new poem")
    public void createTest()
    {
        Poem poem = new Poem("New poem", "hi hi hi hi hi hi", "Lorenzo");
        try
        {
            String json = objectMapper.writeValueAsString(poem);
            given().when()
                .contentType("application/json")
                .accept("application/json")
                .body(json)
                .post("/poem")
                .then()
                .statusCode(200);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            fail();
        }
    }
}