package com.MyP.Zilieri.service.impl;


import com.MyP.Zilieri.entities.Role;
import com.MyP.Zilieri.entities.User;
import com.MyP.Zilieri.repository.UserRepository;
import com.MyP.Zilieri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            }
        };
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User findOrCreateUser(String email, String username, Boolean isExternalAuth) {
        Optional<User> userOptional = findByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get(); // Return the existing user
        } else {
            // Create a new user
            User newUser = new User();

            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setExternalAuth(isExternalAuth);
            newUser.setRole(Role.USER);
            newUser.setActive(true);


            return userRepository.save(newUser); // Save and return the new user
        }
    }

}
