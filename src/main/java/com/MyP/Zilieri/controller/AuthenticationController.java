package com.MyP.Zilieri.controller;


import com.MyP.Zilieri.dto.*;
import com.MyP.Zilieri.entities.User;
import com.MyP.Zilieri.service.AuthenticationService;
import com.MyP.Zilieri.service.UserService;
import com.MyP.Zilieri.validations.RegisterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RegisterValidation registerValidation;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        ResponseEntity<Object> validationResponse = registerValidation.validateRegistration(signupRequest);
        if (signupRequest.getUsername() == null || signupRequest.getUsername().isEmpty() ||
                signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty() ||
                signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: All fields are required!"));
        }else if (validationResponse != null)
        {
            return validationResponse;
        }

        return ResponseEntity.ok(authenticationService.signup(signupRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest){
        try {
            return ResponseEntity.ok(authenticationService.signin(signinRequest));
        } catch (UsernameNotFoundException e) {
            // User not found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: No user found with the provided email."));
        } catch (BadCredentialsException e) {
            // Invalid username or password
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid email or password!"));
        } catch (AuthenticationException e) {
            // Generic authentication failure
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: The account needs to be activated to login."));
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token) {
        Optional<User> userOptional = userService.findByActivationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(true);
            user.setActivationToken(null); // Clear the token
            userService.save(user);
            return ResponseEntity.ok("Account activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation token");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestParam("code") String code) {
        System.out.println("code");

        try {
            System.out.println(code);
            JwtAuthenticationResponse response = authenticationService.authenticateWithGoogle(code);
            System.out.println(response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions (like failed token exchange, user creation, etc.)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Google authentication: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.forgotPassword(email);
            return ResponseEntity.ok(new MessageResponse("Password reset email sent successfully."));
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during password reset: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            authenticationService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("Password reset successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during password reset: " + e.getMessage());
        }
    }
}
