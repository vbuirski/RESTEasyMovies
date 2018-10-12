package au.com.vb.client;

import au.com.vb.model.Movie;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/movies")
public interface ServicesInterface {

  @GET
  @Path("/get/{id}")
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  Movie movieByImdbId(@PathParam("id") String imdbId);

  @GET
  @Path("/list")
  @Produces({ "application/json" })
  List<Movie> listMovies();

  @POST
  @Path("/add")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  Response addMovie(Movie movie);

  @PUT
  @Path("/update")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  Response updateMovie(Movie movie);

  @DELETE
  @Path("/delete")
  Response deleteMovie(@QueryParam("imdbId") String imdbId);

}
