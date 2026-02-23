package org.acme.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.auth.dto.AuthDtos.*;
import org.acme.user.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.util.UUID;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private String signToken(User user) {
        return Jwt.issuer("auth-app")
                .subject(user.id.toString())
                .claim("email", user.email)
                .expiresIn(Duration.ofDays(7))
                .sign();
    }

    @POST
    @Path("/signup")
    @Transactional
    public Response signup(@Valid SignupReq body) {
        User exists = User.find("email", body.email).firstResult();
        if (exists != null) {
            return Response.status(409).entity(new java.util.HashMap<>() {{
                put("message", "Email already exists");
            }}).build();
        }

        User u = new User();
        u.name = body.name;
        u.email = body.email;
        u.passwordHash = BCrypt.hashpw(body.password, BCrypt.gensalt(12));
        u.persist();

        String token = signToken(u);
        return Response.status(201).entity(new AuthRes(token, new UserRes(
                u.id.toString(), u.name, u.email
        ))).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginReq body) {
        User u = User.find("email", body.email).firstResult();
        if (u == null) {
            return Response.status(401).entity(new java.util.HashMap<>() {{
                put("message", "Invalid credentials");
            }}).build();
        }

        boolean ok = BCrypt.checkpw(body.password, u.passwordHash);
        if (!ok) {
            return Response.status(401).entity(new java.util.HashMap<>() {{
                put("message", "Invalid credentials");
            }}).build();
        }

        String token = signToken(u);
        return Response.ok(new AuthRes(token, new UserRes(
                u.id.toString(), u.name, u.email
        ))).build();
    }
}