package au.com.vb.app;

import au.com.vb.rest.MessageRestService;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class MessageApplication extends Application {
  private Set<Object> singletons = new HashSet<Object>();

  public MessageApplication() {
    singletons.add(new MessageRestService());
  }

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }
}

