package org.acme.auth;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkus.security.Authenticated;
import org.acme.auth.dto.AuthDtos.*;
import org.acme.user.User;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    JsonWebToken jwt;

    @GET
    @Path("/me")
    @Authenticated
    public Response me() {
        String userId = jwt.getSubject(); // we used subject = user.id
        User u = User.findById(java.util.UUID.fromString(userId));
        if (u == null) return Response.status(404).entity(new java.util.HashMap<>() {{
            put("message", "User not found");
        }}).build();

        return Response.ok(new java.util.HashMap<>() {{
            put("user", new UserRes(u.id.toString(), u.name, u.email));
        }}).build();
    }

    @GET
    @Path("/users")
    @Authenticated
    public Response users() {
        List<User> list = User.listAll();
        var users = list.stream()
                .map(u -> new UserRes(u.id.toString(), u.name, u.email))
                .toList();
        return Response.ok(new java.util.HashMap<>() {{
            put("users", users);
        }}).build();
    }

    @PATCH
    @Path("/me")
    @Authenticated
    @Transactional
    public Response updateMe(@Valid UpdateMeReq body) {
        String userId = jwt.getSubject();
        User u = User.findById(java.util.UUID.fromString(userId));
        if (u == null) return Response.status(404).entity(new java.util.HashMap<>() {{
            put("message", "User not found");
        }}).build();

        if (body.name != null && !body.name.isBlank()) u.name = body.name;
        if (body.email != null && !body.email.isBlank()) u.email = body.email;

        return Response.ok(new java.util.HashMap<>() {{
            put("user", new UserRes(u.id.toString(), u.name, u.email));
        }}).build();
    }
}