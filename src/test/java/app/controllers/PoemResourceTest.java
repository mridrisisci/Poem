package app.controllers;

import app.entities.Poem;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
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

    }

    @BeforeEach
    void setUp()
    {
        ApplicationConfig
            .getInstance()
            .initiateServer()
            .setRoute(Routes.getRoutes())
            .startServer(7777);
        RestAssured.baseURI = "http://localhost:7777/api";
    }

    @AfterEach
    void tearDown()
    {
        ApplicationConfig.stopServer();
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
                .statusCode(200)
                .body("title", equalTo("Digte 1")); // check here??
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test for updating an existing poem")
    public void updateTest()
    {
        Poem poem = new Poem("New poem2", "hi hi hi hi hi hi", "GPT");
        try
        {
            String json = objectMapper.writeValueAsString(poem);
            given().when()
                .contentType("application/json")
                .accept("application/json")
                .body(json)
                .put("/poem/1") // double check id
                .then()
                .statusCode(200)
                .body("author", equalTo("GPT")); // check here??
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            fail();
        }
    }
}