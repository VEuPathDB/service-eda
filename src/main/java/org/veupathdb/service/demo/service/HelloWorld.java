package org.veupathdb.service.demo.service;

import jakarta.ws.rs.core.Context;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.veupathdb.lib.container.jaxrs.model.User;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.demo.generated.model.HelloPostRequest;
import org.veupathdb.service.demo.generated.model.HelloPostResponseImpl;
import org.veupathdb.service.demo.generated.model.HelloResponse.GreetingType;
import org.veupathdb.service.demo.generated.model.HelloResponseImpl;
import org.veupathdb.service.demo.generated.model.ServerErrorImpl;
import org.veupathdb.service.demo.generated.resources.Hello;

public class HelloWorld implements Hello {

  private static final Logger LOG = LogManager.getLogger(HelloWorld.class);
  @Context
  private ContainerRequest req;

  @Override
  public GetHelloResponse getHello() {
    var out = new HelloResponseImpl();
    out.setGreeting(GreetingType.HELLOWORLD);
    return GetHelloResponse.respond200WithApplicationJson(out);
  }

  @Override
  @Authenticated
  public PostHelloResponse postHello(HelloPostRequest entity) {

    // demonstrate how to handle unknown request property types
    Object config = entity.getConfig();
    LOG.info("config object is " + config.getClass().getName());
    if (config instanceof List<?> list) {
      for (int i = 0; i < list.size(); i++) {
        LOG.info("config[" + i + "] is " + list.get(i).getClass().getName());
      }
    }
    else if (config instanceof Map<?,?> map) {
      int i = 0;
      for (Map.Entry<?,?> entry : map.entrySet()) {
        LOG.info("config entry " + i + ": key is " + entry.getKey().getClass().getName() + ", value is " + entry.getValue().getClass().getName());
        i++;
      }
    }

    // Throw a 500 every once in a while for fun.
    var rand = new Random();
    if (rand.nextInt(4) == 2) {
      var out = new ServerErrorImpl();
      out.setMessage("Whoops!");
      return PostHelloResponse.respond500WithApplicationJson(out);
    }

    var out = new HelloPostResponseImpl();
    out.setMessage(String.format("Hello %s!", UserProvider.lookupUser(req)
      .map(User::getFirstName)
      .orElse("you")));

    return PostHelloResponse.respond200WithApplicationJson(out);
  }
}
