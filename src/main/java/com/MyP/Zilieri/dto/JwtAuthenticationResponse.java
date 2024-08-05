package com.MyP.Zilieri.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private String token;
    private String error;

    private String refreshToken;

    public static JwtAuthenticationResponse error(String error) {
        return JwtAuthenticationResponse.builder().error(error).build();
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
