package au.com.vb.app;

import au.com.vb.rest.MovieRestService;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class MovieApplication extends Application {

  private Set<Object> singletons = new HashSet<Object>();

  public MovieApplication() {

    singletons.add(new MovieRestService());

  }

  @Override
  public Set<Object> getSingletons() {

    return singletons;
  }
}

