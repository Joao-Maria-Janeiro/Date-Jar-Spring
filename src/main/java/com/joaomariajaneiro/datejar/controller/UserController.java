package com.joaomariajaneiro.datejar.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import com.joaomariajaneiro.datejar.security.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/authenticate")
    public String login(@RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jsonNode.get("username").asText(),
                            jsonNode.get("password").asText())
            );
        } catch (BadCredentialsException e) {
            return "Username and password didn't match";
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(jsonNode.get("username").asText());

        return jwtTokenUtil.generateToken(userDetails);
    }
}

