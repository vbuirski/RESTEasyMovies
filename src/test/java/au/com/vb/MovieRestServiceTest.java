package au.com.vb;

import au.com.vb.model.Movie;
import au.com.vb.rest.MovieRestService;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.junit.Before;
import org.junit.Test;
import sun.misc.IOUtils;

public class MovieRestServiceTest {

  public static final UriBuilder FULL_PATH = UriBuilder.fromPath("http://127.0.0.1:8082/RestEasyTutorial/rest");
  Movie heroMovie = null;
  Movie supergirlMovie = null;
  ObjectMapper jsonMapper = null;

  @Before
  public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NamingException {

    jsonMapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jsonMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
    jsonMapper.setDateFormat(sdf);

    try (InputStream inputStream = new MovieRestServiceTest().getClass().getResourceAsStream("./movies/hero.json")) {
      final String transformerMovieAsString = String.format(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
      heroMovie = jsonMapper.readValue(transformerMovieAsString, Movie.class);
    } catch (final Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Test is going to die ...", e);
    }

    try (InputStream inputStream = new MovieRestServiceTest().getClass().getResourceAsStream("./movies/supergirl.json")) {
      final String supergirlMovieAsString = String.format(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
       = jsonMapper.readValue(supergirlMovieAsString, Movie.class);

    } catch (final Exception e) {
      throw new RuntimeException("Test is going to die ...", e);
    }
  }

  @Test
  public void testListAllMovies() {

    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    Response moviesResponse = proxy.addMovie(heroMovie);
    moviesResponse.close();
    moviesResponse = proxy.addMovie();
    moviesResponse.close();

    final List<Movie> movies = proxy.listMovies();
    System.out.println(movies);
  }

  @Test
  public void testMovieByImdbId() {

    final String heroImdbId = "42";

    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    final Response moviesResponse = proxy.addMovie(heroMovie);
    moviesResponse.close();

    final Movie movies = proxy.movieByImdbId(heroImdbId);
    System.out.println(movies);
  }

  @Test
  public void testAddMovie() {

    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    Response moviesResponse = proxy.addMovie();
    moviesResponse.close();
    moviesResponse = proxy.addMovie(heroMovie);

    if (moviesResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
      System.out.println("Failed : HTTP error code : " + moviesResponse.getStatus());
    }

    moviesResponse.close();
    System.out.println("Response Code: " + moviesResponse.getStatus());
  }

  @Test
  public void testAddMovieMultiConnection() {

    final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
    final ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
    final ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    final Response supergirlResponse = proxy.addMovie();
    final Response transformerResponse = proxy.addMovie(heroMovie);

    if (supergirlResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
      System.out.println("Supergirl Movie creation Failed : HTTP error code : " + supergirlResponse.getStatus());
    }
    if (supergirlResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
      System.out.println("Supergirl Movie creation Failed : HTTP error code : " + supergirlResponse.getStatus());
    }

    supergirlResponse.close();
    transformerResponse.close();
    cm.close();

  }

  @Test
  public void testDeleteMovie() {

    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    Response moviesResponse = proxy.addMovie();
    moviesResponse.close();
    moviesResponse = proxy.deleteMovie(supergirlMovie.getImdbId());

    if (moviesResponse.getStatus() != Response.Status.OK.getStatusCode()) {
      System.out.println(moviesResponse.readEntity(String.class));
      throw new RuntimeException("Failed : HTTP error code : " + moviesResponse.getStatus());
    }

    moviesResponse.close();
    System.out.println("Response Code: " + moviesResponse.getStatus());
  }

  @Test
  public void testUpdateMovie() {

    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget target = client.target(FULL_PATH);
    final MovieRestService proxy = target.proxy(MovieRestService.class);

    Response moviesResponse = proxy.addMovie();
    moviesResponse.close();
    supergirlMovie.setTitle("Supergirl Begins");
    moviesResponse = proxy.updateMovie();

    if (moviesResponse.getStatus() != Response.Status.OK.getStatusCode()) {
      System.out.println("Failed : HTTP error code : " + moviesResponse.getStatus());
    }

    moviesResponse.close();
    System.out.println("Response Code: " + moviesResponse.getStatus());
  }
}