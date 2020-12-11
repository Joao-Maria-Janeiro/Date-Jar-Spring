package com.joaomariajaneiro.datejar.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

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
            throw new AuthenticationException();
        }

        final User user = userRepository.findByUsername(jsonNode.get("username").asText());

        JsonObject output = new JsonObject();
        output.addProperty("token", jwtTokenUtil.generateToken(user.getUsername()));
        output.addProperty("picture", user.getPicture());
        return output.toString();
    }

    @PostMapping(value = "/create")
    public String signup(@RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        User user;
        try {
            user = new User(
                    jsonNode.get("username").asText(),
                    jsonNode.get("email").asText(),
                    jsonNode.get("password").asText(),
                    jsonNode.get("picture").asText()
            );
            userRepository.save(user);
        } catch (Exception e) {
            return "An error occurred while creating your account, please try again";
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jsonNode.get("username").asText(),
                            jsonNode.get("password").asText())
            );
        } catch (BadCredentialsException e) {
            return "Your account was created successfully but an error occurred while" +
                    " trying to sign you in. Please sign up manually";
        }

        JsonObject output = new JsonObject();
        output.addProperty("token", jwtTokenUtil.generateToken(user.getUsername()));
        output.addProperty("picture", user.getPicture());
        return output.toString();
    }


    @PostMapping(value = "/associate")
    public String associateUser(@RequestHeader Map<String, String> headers,
                                @RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            userRepository.associateUser(username,
                    jsonNode.get("partner_username").asText());
        } catch (Exception e) {
            return "The user association failed";
        }
        return "Success";
    }

    @GetMapping(value = "/friend")
    public String getAssociatedUser(@RequestHeader Map<String, String> headers) throws JsonProcessingException {
        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            return userRepository.associatedUser(username).getUsername();
        } catch (Exception e) {
            return "There was an error retrieving your friend";
        }
    }

    @PostMapping(value = "/remove-friend")
    public String removeAssociateUser(@RequestHeader Map<String, String> headers) throws JsonProcessingException {
        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            userRepository.removeAssociatedUser(username);
        } catch (Exception e) {
            return "The user removal failed";
        }
        return "Success";
    }

}

