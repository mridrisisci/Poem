package app.controllers;


import app.daos.GenericDAO;
import app.dtos.PoemDTO;
import app.entities.Poem;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PoemController
{

    private GenericDAO genericDAO;

    public PoemController(EntityManagerFactory emf)
    {
        genericDAO = GenericDAO.getInstance(emf);
    }

    public void getAll(Context ctx)
    {
        try
        {
            ctx.json(genericDAO.findAll(Poem.class));
        } catch (Exception e)
        {
            ctx.status(404).json("could not retrieve all objects");
        }
    }

    public void getById(Context ctx)
    {
        try
        {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            PoemDTO foundPoem = new PoemDTO(genericDAO.read(Poem.class, id));
        } catch (Exception e)
        {
            ctx.status(404).json("Poem not found");
        }
    }

    public void create(Context ctx)
    {
        try
        {
            PoemDTO newPoem = ctx.bodyAsClass(PoemDTO.class);
            Poem poem = new Poem(newPoem);
            ctx.json(genericDAO.create(Poem.class));
        } catch (Exception e)
        {
            ctx.status(404).json("could not persist object to db");
        }
    }

    public void update(Context ctx)
    {
        try
        {
            ctx.json(genericDAO.update(Poem.class));
        } catch (Exception e)
        {
            ctx.status(404).json("error updating object");
        }
    }

    public void delete(Context ctx)
    {
        try
        {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            genericDAO.delete(Poem.class, id);
            ctx.status(204);
        } catch (Exception e)
        {
            ctx.status(404).json("error deleting object");
        }
    }

}
