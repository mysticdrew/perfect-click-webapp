package com.webapp.api;

import com.webapp.api.model.PingRequest;
import com.webapp.api.model.PingResponse;
import io.javalin.http.Handler;

public class PingApi {
  private final String environment;
  private final PingService pingService;

  public PingApi(String environment, PingService pingService) {
    this.environment = environment;
    this.pingService = pingService;
  }

  public Handler ping() {
    return ctx -> {
      PingRequest request = ctx.bodyAsClass(PingRequest.class);
      PingResponse response = pingService.ping(request.message(), environment);
      ctx.status(200).json(response);
    };
  }
}
