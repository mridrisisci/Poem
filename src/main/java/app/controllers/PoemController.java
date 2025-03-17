package app.controllers;


import app.daos.GenericDAO;
import app.dtos.ErrorMessage;
import app.dtos.PoemDTO;
import app.entities.Poem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PoemController
{

    private GenericDAO genericDAO;
    private Logger logger = LoggerFactory.getLogger(PoemController.class);
    private ObjectMapper objectMapper = new ObjectMapper();


    private static ArrayList<PoemDTO> poems = new ArrayList();

    public PoemController(EntityManagerFactory emf)
    {
        genericDAO = GenericDAO.getInstance(emf);
    }


    public void getPoems1(Context ctx)
    {
        try
        {
            List<Poem> poems = genericDAO.findAll(Poem.class);
            // convert entities to DTOs
            List<PoemDTO> poemDTOS = poems.stream()
                .map(PoemDTO::new)
                .collect(Collectors.toList());
            ctx.status(200).json(poemDTOS);
            logger.info("Poems have been fetched");
        } catch (Exception e)
        {
            logger.info("Unable to fetch poems");
            ErrorMessage error = new ErrorMessage("could not retrieve all objects");
            ctx.status(404).json(error);
        }
    }
    public void getPoems(Context ctx)
    {
        try
        {
            ctx.json(genericDAO.findAll(Poem.class));
        } catch (Exception e)
        {
            ctx.status(404).json("could not retrieve all objects");
        }
    }

    public void populateData()
    {
        try
        {
            JsonNode node = objectMapper.readTree(new File("src/poems.json")).get("poems");
            Set<PoemDTO> poems = objectMapper.convertValue(node, new TypeReference<Set<PoemDTO>>() {});
            for (PoemDTO poem : poems)
            {
                genericDAO.create(new Poem(poem));
            }
            poems.forEach(System.out::println);
            //PoemContainerDTO[] poems = objectMapper.readValue(new File("src/poems.json"), PoemContainerDTO[].class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getAll(Context ctx)
    {
        try
        {
            logger.info("test logging" + ctx.path());
            // throw new Exception("stor fejl er sket");  // test denne linje
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
            ctx.json(genericDAO.create(newPoem));
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
