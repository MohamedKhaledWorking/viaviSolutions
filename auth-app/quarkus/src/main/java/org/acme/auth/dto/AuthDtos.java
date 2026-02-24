package org.acme.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class SignupReq {
        @NotBlank public String name;
        @NotBlank @Email public String email;
        @NotBlank @Size(min = 6) public String password;
    }

    public static class LoginReq {
        @NotBlank @Email public String email;
        @NotBlank public String password;
    }

    public static class UpdateMeReq {
        public String name;
        public String email;
    }

    public static class UserRes {
        public String id;
        public String name;
        public String email;

        public UserRes(String id, String name, String email) {
            this.id = id; this.name = name; this.email = email;
        }
    }

    public static class AuthRes {
        public String token;
        public UserRes user;

        public AuthRes(String token, UserRes user) {
            this.token = token; this.user = user;
        }
    }
}