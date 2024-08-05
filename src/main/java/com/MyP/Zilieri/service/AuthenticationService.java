package com.MyP.Zilieri.service;


import com.MyP.Zilieri.dto.JwtAuthenticationResponse;
import com.MyP.Zilieri.dto.RefreshTokenRequest;
import com.MyP.Zilieri.dto.SigninRequest;
import com.MyP.Zilieri.dto.SignupRequest;
import com.MyP.Zilieri.entities.User;

public interface AuthenticationService {

    User signup(SignupRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    JwtAuthenticationResponse authenticateWithGoogle(String code);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
