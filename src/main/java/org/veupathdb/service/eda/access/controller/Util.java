package org.veupathdb.service.eda.access.controller;

import jakarta.ws.rs.InternalServerErrorException;

import org.glassfish.jersey.server.ContainerRequest;
import org.veupathdb.lib.container.jaxrs.model.UserInfo;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;

public class Util
{
  private static final Util instance = new Util();

  Util() {}

  public static Util getInstance() {
    return instance;
  }

  public UserInfo mustGetUser(final ContainerRequest req) {
    return UserProvider.lookupUser(req)
      .orElseThrow(InternalServerErrorException::new);
  }

  public static UserInfo requireUser(final ContainerRequest req) {
    return getInstance().mustGetUser(req);
  }
}
