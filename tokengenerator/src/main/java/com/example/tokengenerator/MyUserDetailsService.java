package com.example.tokengenerator;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service

public class MyUserDetailsService implements UserDetailsService {

    //expects the method to load user where ever the user got saved..
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User("foo", "foo",
                new ArrayList<>());
    }


}
