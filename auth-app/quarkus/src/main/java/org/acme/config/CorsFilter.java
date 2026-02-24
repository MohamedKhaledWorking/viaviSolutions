package org.acme.config;

import java.io.IOException;
import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION - 10)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Set<String> ALLOWED = Set.of(
      "http://localhost:5173",
      "http://127.0.0.1:5173"
  );

  private static void add(MultivaluedMap<String, Object> h, String origin) {
    h.putSingle("Access-Control-Allow-Origin", origin);
    h.putSingle("Vary", "Origin");
    h.putSingle("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS");
    h.putSingle("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept,X-Requested-With");
    h.putSingle("Access-Control-Expose-Headers", "Authorization,Content-Type");
    h.putSingle("Access-Control-Max-Age", "86400");
  }

  @Override
  public void filter(ContainerRequestContext req) throws IOException {
    String origin = req.getHeaderString("Origin");

    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
      String o = (origin != null && ALLOWED.contains(origin)) ? origin : "http://localhost:5173";
      req.abortWith(Response.ok()
          .header("Access-Control-Allow-Origin", o)
          .header("Vary", "Origin")
          .header("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS")
          .header("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept,X-Requested-With")
          .header("Access-Control-Expose-Headers", "Authorization,Content-Type")
          .header("Access-Control-Max-Age", "86400")
          .build());
    }
  }

  @Override
  public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
    String origin = req.getHeaderString("Origin");
    if (origin != null && ALLOWED.contains(origin)) add(res.getHeaders(), origin);
  }
}