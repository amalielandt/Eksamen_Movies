/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTO.MovieDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.MovieFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author sofieamalielandt
 */
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MovieFacade facade = MovieFacade.getMovieFacade(emf);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String populateDB() {

        facade.populateDB();
        return "{\"msg\":\"The database is now populated\"}";
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public MovieDTO getMovie(@PathParam("id") long id) {

        return facade.getMovie(id);
    }

    @GET
    @Path("/title/{title}")
    @Produces({MediaType.APPLICATION_JSON})
    public MovieDTO getMovie(@PathParam("title") String title) {

        return facade.getMovieByTitle(title);
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MovieDTO> getAllMovies() {

        return facade.getAllMovies();
    }

    @GET
    @Path("/actor/{actor}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MovieDTO> getMoviesByActor(@PathParam("actor") String actor) {

        return facade.getPersonsByActor(actor);
    }

    @GET
    @Path("/director/{director}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MovieDTO> getMoviesByDirector(@PathParam("director") String director) {

        return facade.getPersonsByDirector(director);
    }
    
    @GET
    @Path("/genre/{genre}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MovieDTO> getMoviesByGenre(@PathParam("genre") String genre) {

        return facade.getPersonsByGenre(genre);
    }

}
