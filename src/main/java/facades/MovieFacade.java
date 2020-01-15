/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.MovieDTO;
import entities.Actor;
import entities.Director;
import entities.Genre;
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

    public List<MovieDTO> getMovie(long id) {

        EntityManager em = getEntityManager();
        List<MovieDTO> moviesDTO = new ArrayList();

        try {
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                moviesDTO.add(new MovieDTO(movie));
            }
            return moviesDTO;

        } finally {
            em.close();
        }
    }

    public List<MovieDTO> getMovieByTitle(String title) {

        EntityManager em = getEntityManager();
        List<MovieDTO> moviesDTO = new ArrayList();

        try {

            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.title = :title", Movie.class);
            List<Movie> movies = query.setParameter("title", title).getResultList();

            movies.forEach((movie) -> {
                moviesDTO.add(new MovieDTO(movie));
            });

            return moviesDTO;

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

    public void populateDB() {
        EntityManager em = emf.createEntityManager();

        Actor a1 = new Actor("Johnny Depp", "John Christopher Depp II (born June 9, 1963) is an American actor, producer, and musician. He has been nominated for 10 Golden Globe Awards,");
        Actor a2 = new Actor("Leonardo DiCaprio", "Leonardo Wilhelm DiCaprio (born November 11, 1974) is an American actor, producer, and environmentalist.");
        Actor a3 = new Actor("Will Smith", "Willard Carroll Smith Jr. (born September 25, 1968) is an American actor and rapper. In April 2007, Newsweek called him \"the most powerful actor in Hollywood\".");
        Actor a4 = new Actor("Margot Robbie", "Margot Elise Robbie (born 2 July 1990) is an Australian actress and film producer. She has received nominations for two Academy Awards and five BAFTA Awards.");

        Director d1 = new Director("Steven Spielberg", "Steven Allan Spielberg (born December 18, 1946) is an American filmmaker. He is considered one of the founding pioneers of the New Hollywood era and one of the most popular directors and producers in film history.");
        Director d2 = new Director("Quentin Tarantino", "Quentin Jerome Tarantino (born March 27, 1963) is an American filmmaker, actor, film programmer, and cinema owner. His films are characterized by nonlinear storylines, satirical subject matter, aestheticization of violence.");
        Director d3 = new Director("Tim Burton", "Timothy Walter Burton (born August 25, 1958) is an American director, producer, artist, writer, and animator. He is known for his dark, gothic, and eccentric horror and fantasy films such as Beetlejuice (1988) and Edward Scissorhands (1990).");

        Genre g1 = new Genre("Comedy");
        Genre g2 = new Genre("Drama");
        Genre g3 = new Genre("Thriller");

        Movie m1 = new Movie("Edward Scissorhands", 1990, 4);
        Movie m2 = new Movie("Once Upon a Time in Hollywood", 2019, 3);
        Movie m3 = new Movie("Men in Black", 1997, 5);

        m1.addActor(a1);
        m1.addDirector(d3);
        m1.addDirector(d1);
        m1.addGenre(g1);
        m1.addGenre(g2);

        m2.addActor(a2);
        m2.addActor(a4);
        m2.addDirector(d2);
        m2.addGenre(g2);
        m2.addGenre(g3);

        m3.addActor(a3);
        m3.addDirector(d1);
        m3.addGenre(g1);
        m3.addGenre(g2);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.createNamedQuery("Genre.deleteAllRows").executeUpdate();
            em.createNamedQuery("Actor.deleteAllRows").executeUpdate();
            em.createNamedQuery("Director.deleteAllRows").executeUpdate();

            em.persist(m1);
            em.persist(m2);
            em.persist(m3);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}
