package com.cvgenie.backend.jwt;

import lombok.*;

@Builder
public class JwtResponse {
    private long userId;
    private String jwtToken;



    public JwtResponse() {
    }

    public JwtResponse(long userId, String jwtToken) {
        this.userId = userId;
        this.jwtToken = jwtToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "userId=" + userId +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
