/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.MovieDTO;
import entities.Movie;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author sofieamalielandt
 */
public class MovieFacade {
    
    private static EntityManagerFactory emf;
    private static MovieFacade instance;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public MovieDTO getMovie(long id)  {

        EntityManager em = getEntityManager();
        try {
            Movie movie = em.find(Movie.class, id);
           
            MovieDTO movieDTO = new MovieDTO(movie);
            return movieDTO;

        } finally {
            em.close();
        }
    }
    
    public MovieDTO getMovieByTitle(String title)  {

        EntityManager em = getEntityManager();
        try {
            Movie movie = em.find(Movie.class, title);
           
            MovieDTO movieDTO = new MovieDTO(movie);
            return movieDTO;

        } finally {
            em.close();
        }
    }
    
    public List<MovieDTO> getAllMovies() {

        EntityManager em = getEntityManager();
        List<MovieDTO> moviesDTO = new ArrayList();

        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        List<Movie> movies = query.getResultList();

        movies.forEach((movie) -> {
            moviesDTO.add(new MovieDTO(movie));
        });

        return moviesDTO;

    }
    
    public List<MovieDTO> getPersonsByActor(String actor) {

        EntityManager em = getEntityManager();

        List<MovieDTO> moviesDTO = new ArrayList();

        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m INNER JOIN m.actors ma WHERE ma.name = :actor", Movie.class);
        List<Movie> movies = query.setParameter("actor", actor).getResultList();

        movies.forEach((movie) -> {
            moviesDTO.add(new MovieDTO(movie));
        });
        return moviesDTO;

    }
    
    public List<MovieDTO> getPersonsByDirector(String director) {

        EntityManager em = getEntityManager();

        List<MovieDTO> moviesDTO = new ArrayList();

        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m INNER JOIN m.directors md WHERE md.name = :director", Movie.class);
        List<Movie> movies = query.setParameter("director", director).getResultList();

        movies.forEach((movie) -> {
            moviesDTO.add(new MovieDTO(movie));
        });
        return moviesDTO;

    }
    
    public List<MovieDTO> getPersonsByGenre(String genre) {

        EntityManager em = getEntityManager();

        List<MovieDTO> moviesDTO = new ArrayList();

        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m INNER JOIN m.genres mg WHERE mg.name = :genre", Movie.class);
        List<Movie> movies = query.setParameter("genre", genre).getResultList();

        movies.forEach((movie) -> {
            moviesDTO.add(new MovieDTO(movie));
        });
        return moviesDTO;
    }
}
