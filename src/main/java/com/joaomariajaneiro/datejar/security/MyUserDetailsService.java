package com.joaomariajaneiro.datejar.security;

import com.joaomariajaneiro.datejar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        com.joaomariajaneiro.datejar.model.User user = userRepository.findByUsername(s);
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
