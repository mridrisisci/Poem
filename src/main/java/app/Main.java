package app;

import app.config.HibernateConfig;
import app.controllers.PoemController;
import app.daos.SecurityDAO;
import app.config.ApplicationConfig;
import app.rest.Routes;
import app.security.controllers.SecurityController;
import jakarta.persistence.EntityManagerFactory;
public class Main
{
    final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    final static SecurityController securityController = new SecurityController(emf);
    // initialize controllers vars here
    final static PoemController poemController = new PoemController(emf);

    static SecurityDAO securityDAO = new SecurityDAO(emf);

    public static void main(String[] args)
    {

        // set controllers here
        Routes.setSecurityController(securityController);
        Routes.setPoemController(poemController);


        ApplicationConfig
            .getInstance()
            .initiateServer()
            .checkSecurityRoles()
            .setRoute(Routes.getRoutes())
            .handleExceptions()
            .startServer(7000);

    }
}