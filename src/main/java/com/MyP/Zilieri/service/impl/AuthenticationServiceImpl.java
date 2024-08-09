package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.dto.JwtAuthenticationResponse;
import com.MyP.Zilieri.dto.RefreshTokenRequest;
import com.MyP.Zilieri.dto.SigninRequest;
import com.MyP.Zilieri.dto.SignupRequest;
import com.MyP.Zilieri.entities.Role;
import com.MyP.Zilieri.entities.User;
import com.MyP.Zilieri.repository.UserRepository;
import com.MyP.Zilieri.service.AuthenticationService;
import com.MyP.Zilieri.service.EmailService;
import com.MyP.Zilieri.service.JWTService;
import com.MyP.Zilieri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${google.oauth2.token-endpoint}")
    private String googleTokenEndpoint;

    @Value("${google.oauth2.userinfo-endpoint}")
    private String googleUserinfoEndpoint;

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    @Value("${google.oauth2.redirect-uri}")
    private String redirectUri;

    public User signup(SignupRequest signupRequest){
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setRole(Role.USER);

        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        user.setActive(false);


        String activationLink = "http://localhost:8080/activate?token=" + user.getActivationToken();
        emailService.sendEmail(
                user.getEmail(),
                "Registration Confirmation",
                "Thank you for registering. Please click on the below link to activate your account: " + activationLink
        );
        return userRepository.save(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        try {
            // Load the user details from the database
            User user = userRepository.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + signinRequest.getEmail() + " does not exist."));

            // Check if the account is active
            if (!user.isActive()) {
                throw new AuthenticationException("Account is not active. Please activate your account.") {};
            }

            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

            // Generate JWT and Refresh Token
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(signinRequest.getEmail());
            var jwt = jwtService.generateToken(userDetails);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

            // Return the response
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);

            return jwtAuthenticationResponse;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User with email " + signinRequest.getEmail() + " does not exist.");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Account is not active. Please activate your account.") {};
        }
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }

        return null;
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Check if the account is authenticated externally
        if (user.isExternalAuth()) {
            throw new IllegalStateException("Password reset is not available for externally authenticated accounts.");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        String resetLink = "http://localhost:8080/api/v1/auth/reset-password?token=" + resetToken;
        emailService.sendEmail(
                email,
                "Password Reset Request",
                "To reset your password, click the link below:\n" + resetLink
        );
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid reset token"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetToken(null); // Clear the reset token after successful password reset
        userRepository.save(user);
    }


    public JwtAuthenticationResponse authenticateWithGoogle(String code) {
        try {
            System.out.println(code);

            String accessToken = exchangeCodeForAccessToken(code);
            System.out.println(accessToken);

            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Failed to exchange code for access token.");
                // Handle the error appropriately
                return null; // Or throw an exception
            }


            GoogleUser googleUser = fetchGoogleUserDetails(accessToken);

            User user = userService.findOrCreateUser(googleUser.getEmail(), googleUser.getEmail().split("@")[0], googleUser.isExternalAuth());
            user.setActive(true);

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getEmail());

            String jwtToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

            return JwtAuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            // Handle the error appropriately
            return null; // Or throw a custom exception
        }
    }

//    public JwtAuthenticationResponse authenticateWithGoogleNative(String code) {
//        try {
//            String accessToken = code;
//
//            if (accessToken == null || accessToken.isEmpty()) {
//                System.out.println("Failed to exchange code for access token.");
//                // Handle the error appropriately
//                return null; // Or throw an exception
//            }
//
//            GoogleUser googleUser = fetchGoogleUserDetails(accessToken);
//
//            User user = userService.findOrCreateUser(googleUser.getEmail(), googleUser.getEmail().split("@")[0], googleUser.isExternalAuth(), googleUser.getTokens());
//
//            UserDetails userDetails = userDetailServiceImp.loadUserByUsernameGoogle(user.getEmail());
//
//            String jwtToken = jwtService.generateToken(userDetails);
//            String refreshToken = jwtService.generateRefreshToken(userDetails);
//
//            return JwtAuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .refreshToken(refreshToken)
//                    .build();
//        } catch (Exception e) {
//            System.out.println("Error during authentication: " + e.getMessage());
//            e.printStackTrace();
//            // Handle the error appropriately
//            return null; // Or throw a custom exception
//        }
//    }

    private String exchangeCodeForAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);
        params.add("access_type", "offline");
        params.add("code", code);
        System.out.println("params");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        System.out.println("headers");

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(googleTokenEndpoint, requestEntity, Map.class);
        System.out.println(responseEntity);

        Map<String, Object> responseMap = responseEntity.getBody();
        return responseMap != null ? (String) responseMap.get("access_token") : null;
    }



    private GoogleUser fetchGoogleUserDetails(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUser> response = restTemplate.exchange(googleUserinfoEndpoint, HttpMethod.GET, entity, GoogleUser.class);
        return response.getBody();
    }

    // Static nested class representing the structure of a Google user's data.
    private static class GoogleUser {
        private Integer id;
        private String email;
        private String username;


        private boolean isExternalAuth;

        public boolean isExternalAuth() {
            return true;
        }

        public void setExternalAuth(boolean externalAuth) {
            isExternalAuth = externalAuth;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
// Getters and setters
    }
}
