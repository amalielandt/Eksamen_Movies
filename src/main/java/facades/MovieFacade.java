/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.ADDTO;
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
import javax.ws.rs.WebApplicationException;

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

    public MovieDTO addMovie(MovieDTO movie) throws NotFoundException {

        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            //Checking if title is not already used
            List<MovieDTO> movies = getAllMovies();
            for (MovieDTO m : movies) {

                if (movie.getTitle().toLowerCase().equals(m.getTitle().toLowerCase()) && movie.getId() != m.getId()) {
                    throw new NotFoundException("The movie title is already in the system");
                }
            }

            Movie newMovie = new Movie(movie.getTitle(), movie.getYear(), movie.getVotes());

            em.persist(newMovie);
            em.getTransaction().commit();

            return new MovieDTO(newMovie);

        } finally {
            em.close();
        }
    }

    public MovieDTO editMovie(MovieDTO newMovie) throws NotFoundException {

        EntityManager em = getEntityManager();
        Movie movie = em.find(Movie.class, newMovie.getId());
        if (movie == null) {

            throw new WebApplicationException("No movie with the given id was found");
        }
       
        try {
            em.getTransaction().begin();
            
            //Checking if information is not already used
            List<MovieDTO> movies = getAllMovies();
            for (MovieDTO m : movies) {

                if (newMovie.getTitle().toLowerCase().equals(m.getTitle().toLowerCase()) && newMovie.getId() != m.getId()) {
                    throw new NotFoundException("The movie title is already in the system");
                }
            }

            movie.setTitle(newMovie.getTitle());
            movie.setYear(newMovie.getYear());
            movie.setVotes(newMovie.getVotes());

            em.merge(movie);
            em.persist(movie);
            em.getTransaction().commit();

            return new MovieDTO(movie);

        } finally {
            em.close();
        }
    }

    public void deleteMovie(long id) throws NotFoundException {
        EntityManager em = getEntityManager();
        Movie movie;

        try {
            em.getTransaction().begin();
            movie = em.find(Movie.class, id);

            em.remove(movie);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new NotFoundException("The movie could not be deleted");
        } finally {
            em.close();
        }
    }
    
    public void addActorToMovie(long movie_id, long actor_id) throws NotFoundException {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, actor_id);
            if (actor == null) {

                throw new NotFoundException("Actor does not exsist");
            }

            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            movie.addActor(actor);

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    
    public void removeActorFromMovie(long movie_id, long actor_id ) throws NotFoundException {
        EntityManager em = getEntityManager();
        Actor actorToRemove = null;

        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            for (Actor a : movie.getActors()) {

                if (a.getId() == actor_id) {
                    actorToRemove = a;
                }
            }

            if (actorToRemove != null) {
                movie.removeActor(actorToRemove);
            }

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    
    public void addDirectorToMovie(long movie_id, long director_id) throws NotFoundException {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            Director director = em.find(Director.class, director_id);
            if (director == null) {

                throw new NotFoundException("Director does not exsist");
            }

            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            movie.addDirector(director);

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    
    public void removeDirectorFromMovie(long movie_id, long director_id ) throws NotFoundException {
        EntityManager em = getEntityManager();
        Director directorToRemove = null;

        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            for (Director d : movie.getDirectors()) {

                if (d.getId() == director_id) {
                   directorToRemove = d;
                }
            }

            if (directorToRemove != null) {
                movie.removeDirector(directorToRemove);
            }

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    
    public void addGenreToMovie(long movie_id, long genre_id) throws NotFoundException {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            Genre genre = em.find(Genre.class, genre_id);
            if (genre == null) {

                throw new NotFoundException("Genre does not exsist");
            }

            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            movie.addGenre(genre);

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    
    public void removeGenreFromMovie(long movie_id, long genre_id ) throws NotFoundException {
        EntityManager em = getEntityManager();
        Genre genreToRemove = null;

        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, movie_id);
            if (movie == null) {

                throw new NotFoundException("Movie does not exsist");
            }

            for (Genre g : movie.getGenres()) {

                if (g.getId() == genre_id) {
                   genreToRemove = g;
                }
            }

            if (genreToRemove != null) {
                movie.removeGenre(genreToRemove);
            }

            em.merge(movie);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
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
