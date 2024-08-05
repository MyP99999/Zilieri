package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        // Extract email or other details from oidcUser
        String email = oidcUser.getEmail();

        // Use email to load or create a user
        UserDetails user = userService.userDetailsService().loadUserByUsername(email);

        // ... additional processing if needed

        return oidcUser;
    }
}
