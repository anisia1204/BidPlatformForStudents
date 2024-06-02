package com.licenta.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
    @JsonProperty("token")
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
