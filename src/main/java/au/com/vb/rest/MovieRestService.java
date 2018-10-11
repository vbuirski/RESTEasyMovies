package au.com.vb.rest;

import au.com.vb.model.Movie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//http://localhost:8090/RESTfulExample/rest/message/hello%20world
@Path("/movies")
public class MovieRestService {

  private Map<String, Movie> inventory = new HashMap<String, Movie>();

  @GET
  @Path("/message/{param}")
  public Response printMessage(@PathParam("param") String msg) {

    String result = "RESTEasy example : " + msg;

    return Response.status(200).entity(result).build();

  }

  @GET
  @Path("/get/{id}")
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Movie movieByImdbId(@PathParam("id") String imdbId) {

    System.out.println("*** Calling get for a given ImdbID***");

    if (inventory.containsKey(imdbId)) {
      return inventory.get(imdbId);
    } else
      return null;
    }

  @POST
  @Path("/add")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response addMovie(Movie movie) {

    System.out.println("*** Calling add ***");

    if (null != inventory.get(movie.getImdbId())) {
      return Response.status(Response.Status.NOT_MODIFIED).entity("Movie is Already in the database.").build();
    }

    inventory.put(movie.getImdbId(), movie);
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Path("/update")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response updateMovie(Movie movie) {

    System.out.println("*** Calling update ***");

    if (null == inventory.get(movie.getImdbId())) {
      return Response.status(Response.Status.NOT_MODIFIED)
              .entity("Movie is not in the database.\nUnable to Update").build();
    }

    inventory.put(movie.getImdbId(), movie);
    return Response.status(Response.Status.OK).build();
  }

  @DELETE
  @Path("/delete")
  public Response deleteMovie(@QueryParam("imdbId") String imdbId) {

    System.out.println("*** Calling delete ***");

    if (null == inventory.get(imdbId)) {
      return Response.status(Response.Status.NOT_FOUND).entity("Movie is not in the database.\nUnable to Delete")
              .build();
    }

    inventory.remove(imdbId);
    return Response.status(Response.Status.OK).build();
  }

  @GET
  @Path("/list")
  @Produces({ "application/json" })
  public List<Movie> listMovies() {

    return inventory.values().stream().collect(Collectors.toCollection(ArrayList::new));
  }
}
