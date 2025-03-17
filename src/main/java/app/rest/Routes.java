package app.rest;

import app.config.HibernateConfig;
import app.controllers.PoemController;
import app.enums.Role;
import app.security.controllers.ISecurityController;
import app.security.controllers.SecurityController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static ISecurityController securityController;
    private static ObjectMapper objectMapper = new ObjectMapper();
    // CONTROLLER HERE
    private static PoemController poemController;
/*
    public Routes(PoemController poemController, SecurityController securityController)
    {
        Routes.poemController = poemController;
        Routes.securityController = securityController;
    }*/

    public static EndpointGroup getRoutes()
    {
        return () ->
        {
            path("/api", () ->
            {
               get("/", poemController::getPoems1);
               get("/poem/{id}", poemController::getById);
               post("/poem", poemController::create);
               put("/poem/{id}", poemController::update);
               delete("/poem/{id}", poemController::delete);
            });
            path("/auth", () ->
            {
                post("/register", securityController.register());
                post("/login", securityController.login());
            });
            path("/secured", () ->
            {
                get("demo", ctx -> ctx.json(objectMapper.createObjectNode().put("demo","its friday bitch")), Role.ACCOUNT);

            });
        };
    }


    public static void setSecurityController(SecurityController securityController)
    {
        Routes.securityController = securityController;
    }

    public static void setPoemController(PoemController poemController)
    {
        Routes.poemController = poemController;
    }

}
