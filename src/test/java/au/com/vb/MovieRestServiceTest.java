package au.com.vb;

import au.com.vb.model.Movie;
import au.com.vb.rest.MovieRestService;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class MovieRestServiceTest {

    @InjectMocks
    public static MovieRestService movieRestService = new MovieRestService();

    Movie heroMovie = null;
    Movie supergirlMovie = null;
    ObjectMapper jsonMapper = null;

    @Before
    public void setup() {

        jsonMapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        jsonMapper.setDateFormat(sdf);

        try (InputStream inputStream = MovieRestServiceTest.class.getResourceAsStream("/movies/hero.json")) {
            final String heroMovieAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8.toString());
            heroMovie = jsonMapper.readValue(heroMovieAsString, Movie.class);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Test is going to die ...", e);
        }

        try (InputStream inputStream = MovieRestServiceTest.class.getResourceAsStream("/movies/supergirl.json")) {
            final String supergirlMovieAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8.toString());
            supergirlMovie = jsonMapper.readValue(supergirlMovieAsString, Movie.class);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Test is going to die ...", e);
        }

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListAllMovies() {

        movieRestService.addMovie(heroMovie);
        movieRestService.addMovie(supergirlMovie);

        final List<Movie> movies = movieRestService.listMovies();
        System.out.println(movies);
    }

    @Test
    public void testMovieByImdbId() {

        final String heroImdbId = "42";

        movieRestService.addMovie(heroMovie);
        movieRestService.movieByImdbId(heroImdbId);
    }

  @Test
  public void testAddMovie() {

    Response addMovieResponse = movieRestService.addMovie(heroMovie);
    if (addMovieResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
      System.out.println("Failed : HTTP error code : " + addMovieResponse.getStatus());
    }

    addMovieResponse.close();
    System.out.println("Response Code: " + addMovieResponse.getStatus());
  }

  @Test
  public void testDeleteMovie() {

    movieRestService.addMovie(supergirlMovie);

    Response deleteMovieResponse = movieRestService.deleteMovie(supergirlMovie.getImdbId());
    if (deleteMovieResponse.getStatus() != Response.Status.OK.getStatusCode()) {
      System.out.println(deleteMovieResponse.readEntity(String.class));
      throw new RuntimeException("Failed : HTTP error code : " + deleteMovieResponse.getStatus());
    }

    System.out.println("Response Code: " + deleteMovieResponse.getStatus());
  }

  @Test
  public void testUpdateMovie() {

    Response moviesResponse = movieRestService.addMovie(supergirlMovie);
    supergirlMovie.setTitle("Supergirl Begins");
    moviesResponse = movieRestService.updateMovie(supergirlMovie);

    if (moviesResponse.getStatus() != Response.Status.OK.getStatusCode()) {
      System.out.println("Failed : HTTP error code : " + moviesResponse.getStatus());
    }

    moviesResponse.close();
    System.out.println("Response Code: " + moviesResponse.getStatus());
  }

}