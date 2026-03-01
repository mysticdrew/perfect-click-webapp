package com.webapp.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webapp.config.AppConfig;
import io.javalin.Javalin;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class WebServerTest {
  @Test
  void healthEndpointReturnsOk() throws IOException, InterruptedException {
    Javalin app = WebServer.create(new AppConfig(0, "test")).start(0);
    try {
      HttpResponse<String> response = sendRequest(app, "/health", "GET", null, null);
      assertEquals(200, response.statusCode(), response.body());
      assertTrue(response.body().contains("\"status\":\"ok\""));
      assertTrue(response.body().contains("\"environment\":\"test\""));
    } finally {
      app.stop();
    }
  }

  @Test
  void pingEndpointReturnsPayload() throws IOException, InterruptedException {
    Javalin app = WebServer.create(new AppConfig(0, "test")).start(0);
    try {
      HttpResponse<String> response =
          sendRequest(app, "/api/v1/ping", "POST", "{\"message\":\"hello\"}", null);
      assertEquals(200, response.statusCode(), response.body());
      assertTrue(response.body().contains("\"message\":\"hello\""));
      assertTrue(response.body().contains("\"environment\":\"test\""));
      assertTrue(response.body().contains("\"timestamp\""));
    } finally {
      app.stop();
    }
  }

  @Test
  void pingEndpointValidatesMessage() throws IOException, InterruptedException {
    Javalin app = WebServer.create(new AppConfig(0, "test")).start(0);
    try {
      HttpResponse<String> response =
          sendRequest(app, "/api/v1/ping", "POST", "{\"message\":\"\"}", null);
      assertEquals(400, response.statusCode(), response.body());
      assertTrue(response.body().contains("\"error\""));
      assertTrue(response.body().contains("\"requestId\""));
    } finally {
      app.stop();
    }
  }

  @Test
  void requestIdHeaderIsPropagated() throws IOException, InterruptedException {
    Javalin app = WebServer.create(new AppConfig(0, "test")).start(0);
    try {
      HttpResponse<String> response = sendRequest(app, "/health", "GET", null, "req-123");
      assertEquals(200, response.statusCode(), response.body());
      assertEquals("req-123", response.headers().firstValue("X-Request-Id").orElse(null));
    } finally {
      app.stop();
    }
  }

  private HttpResponse<String> sendRequest(
      Javalin app, String path, String method, String jsonBody, String requestId)
      throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    String baseUrl = "http://localhost:" + app.port();
    HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path));

    if (requestId != null) {
      builder.header("X-Request-Id", requestId);
    }

    if ("POST".equals(method)) {
      builder.header("Content-Type", "application/json");
      builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
    } else {
      builder.GET();
    }

    return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
  }
}
