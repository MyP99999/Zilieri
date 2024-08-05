package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {


    UserDetailsService userDetailsService();

    User findOrCreateUser(String email, String username, Boolean isExternalAuth);
}
