package app;

import app.config.HibernateConfig;
import app.controllers.PoemController;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main
{
    final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    final static PoemController poemController = new PoemController(emf);

    public static void main(String[] args)
    {
        poemController.populateData();
        ApplicationConfig
            .getInstance()
            .initiateServer()
            .setRoute(Routes.getRoutes())
            .handleExceptions()
            .startServer(7000);
    }
}