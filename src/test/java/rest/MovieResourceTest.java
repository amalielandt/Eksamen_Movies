/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTO.MovieDTO;
import entities.Actor;
import entities.Director;
import entities.Genre;
import entities.Movie;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import utils.EMF_Creator;

/**
 *
 * @author sofieamalielandt
 */
public class MovieResourceTest {

    private static EntityManagerFactory emf;

    private Movie m1;
    private Movie m2;
    private Movie m3;

    private Genre g1;
    private Genre g2;
    private Genre g3;

    private Actor a1;
    private Actor a2;
    private Actor a3;
    private Actor a4;

    private Director d1;
    private Director d2;
    private Director d3;

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {

        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void tearDownClass() {

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {

        EntityManager em = emf.createEntityManager();

        a1 = new Actor("Johnny Depp", "John Christopher Depp II (born June 9, 1963) is an American actor, producer, and musician. He has been nominated for 10 Golden Globe Awards,");
        a2 = new Actor("Leonardo DiCaprio", "Leonardo Wilhelm DiCaprio (born November 11, 1974) is an American actor, producer, and environmentalist.");
        a3 = new Actor("Will Smith", "Willard Carroll Smith Jr. (born September 25, 1968) is an American actor and rapper. In April 2007, Newsweek called him \"the most powerful actor in Hollywood\".");
        a4 = new Actor("Margot Robbie", "Margot Elise Robbie (born 2 July 1990) is an Australian actress and film producer. She has received nominations for two Academy Awards and five BAFTA Awards.");

        d1 = new Director("Steven Spielberg", "Steven Allan Spielberg (born December 18, 1946) is an American filmmaker. He is considered one of the founding pioneers of the New Hollywood era and one of the most popular directors and producers in film history.");
        d2 = new Director("Quentin Tarantino", "Quentin Jerome Tarantino (born March 27, 1963) is an American filmmaker, actor, film programmer, and cinema owner. His films are characterized by nonlinear storylines, satirical subject matter, aestheticization of violence.");
        d3 = new Director("Tim Burton", "Timothy Walter Burton (born August 25, 1958) is an American director, producer, artist, writer, and animator. He is known for his dark, gothic, and eccentric horror and fantasy films such as Beetlejuice (1988) and Edward Scissorhands (1990).");

        g1 = new Genre("Comedy");
        g2 = new Genre("Drama");
        g3 = new Genre("Thriller");

        m1 = new Movie("Edward Scissorhands", 1990, 4);
        m2 = new Movie("Once Upon a Time in Hollywood", 2019, 3);
        m3 = new Movie("Men in Black", 1997, 5);

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

    /**
     * Test of getMovie method, of class MovieResource.
     */
    @Test
    public void testGetMovie_id() {
        System.out.println("getMovie");

        given()
                .contentType("application/json")
                .get("/movie/" + m1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle()),
                        "year", hasItems(m1.getYear()));
    }

    /**
     * Test of getMovie method, of class MovieResource.
     */
    @Test
    public void testGetMovie_title() {
        System.out.println("getMovie");

        given()
                .contentType("application/json")
                .get("/movie/title/" + m1.getTitle()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle()),
                        "year", hasItems(m1.getYear()));
    }

    /**
     * Test of getAllMovies method, of class MovieResource.
     */
    @Test
    public void testGetAllMovies() {
        System.out.println("getAllMovies");

        given().contentType("application/json")
                .get("/movie/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle(), m2.getTitle(), m3.getTitle()),
                        "year", hasItems(m1.getYear(), m2.getYear(), m3.getYear()));
    }

    /**
     * Test of getMoviesByActor method, of class MovieResource.
     */
    @Test
    public void testGetMoviesByActor() {
        System.out.println("getMoviesByActor");

        given().contentType("application/json")
                .get("/movie/actor/" + a2.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m2.getTitle()),
                        "year", hasItems(m2.getYear()));
    }

    /**
     * Test of getMoviesByDirector method, of class MovieResource.
     */
    @Test
    public void testGetMoviesByDirector() {
        System.out.println("getMoviesByDirector");

        given().contentType("application/json")
                .get("/movie/director/" + d1.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle(), m3.getTitle()),
                        "year", hasItems(m1.getYear(), m3.getYear()));
    }

    /**
     * Test of getMoviesByGenre method, of class MovieResource.
     */
    @Test
    public void testGetMoviesByGenre() {
        System.out.println("getMoviesByGenre");

        given().contentType("application/json")
                .get("/movie/genre/" + g2.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle(), m2.getTitle(), m3.getTitle()),
                        "year", hasItems(m1.getYear(), m2.getYear(), m3.getYear()));
    }

}
