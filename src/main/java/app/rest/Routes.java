package app.rest;

import app.config.HibernateConfig;
import app.controllers.PoemController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static PoemController poemController = new PoemController(emf);

    public static EndpointGroup getRoutes()
    {
        return () ->
        {
            path("/poem", () ->
            {
                get("/", poemController::getAll);
                get("/{id}", poemController::getById);
                post("/",  poemController::create);
                put("/{id}", poemController::update);
                delete("/{id}", poemController::delete);
            });
        };
    }
}
