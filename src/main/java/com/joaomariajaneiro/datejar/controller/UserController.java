package com.joaomariajaneiro.datejar.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import com.joaomariajaneiro.datejar.utils.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(value = "/authenticate")
    public String login(@RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jsonNode.get("username").asText(),
                            jsonNode.get("password").asText())
            );
        } catch (Exception e) {
            throw new AuthenticationException();
        }

        User user;
        try {
            user = userRepository.findByUsername(jsonNode.get("username").asText());
        } catch (Exception e) {
            throw new AuthenticationException();
        }


        return genereateAuthOutput(user).toString();
    }

    @PostMapping(value = "/create")
    public String signup(@RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        EmailValidator validator = EmailValidator.getInstance();

        if (!validator.isValid(jsonNode.get("email").asText())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided email is not " +
                    "valid");
        }


        User user;
        try {
            user = new User(
                    jsonNode.get("username").asText(),
                    jsonNode.get("email").asText(),
                    passwordEncoder.encode(jsonNode.get("password").asText()),
                    jsonNode.get("picture").asText()
            );
            userRepository.save(user);
        } catch (DataAccessException e) {
            String trim = e.getCause().getLocalizedMessage().split("Detail:")[1].trim();
            trim = trim.substring(trim.length() - 16);
            if (trim.contains("already exists.")) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username and/or " +
                        "email" +
                        " already taken");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error " +
                        "occurred while creating your account, please try again");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error " +
                    "occurred while creating your account, please try again");

        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jsonNode.get("username").asText(),
                            jsonNode.get("password").asText())
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account was created " +
                    "successfully but an error occurred while" +
                    " trying to sign you in. Please try to sign in manually");
        }

        return genereateAuthOutput(user).toString();
    }


    @PostMapping(value = "/associate")
    public String associateUser(@RequestHeader Map<String, String> headers,
                                @RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));


            String partnerEmail = userRepository.findByUsername(jsonNode.get(
                    "partner_username").asText()).getEmail();
            String email = userRepository.findByUsername(username).getEmail();
            SendEmail sendEmail = new SendEmail(partnerEmail, "localhost");

            sendEmail.sendMail(username + " wants to be your friend on Me2",
                    "http://192.168.1.8:8080/users/confirm-friend/" + email + "/" + partnerEmail);
        } catch (Exception e) {
            return "The user association request failed";
        }
        return "Success";
    }

    @GetMapping(value = "/confirm-friend/{email}/{partnerEmail}")
    public String confirmAssociateUSer(@RequestHeader Map<String, String> headers,
                                       @PathVariable String email,
                                       @PathVariable String partnerEmail) throws JsonProcessingException {
        try {

            userRepository.associateUser(email,
                    partnerEmail);
            userRepository.associateUser(partnerEmail,
                    email);

            SendEmail sendEmail = new SendEmail(email, "localhost");

            User user = userRepository.findByEmail(partnerEmail);

            sendEmail.sendMail(user.getUsername() + " is now your friend on Me2",
                    "Congratulations getting your friend on Me2!\n Time to start hanging out, add" +
                            " your first category and activity now!");

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

            User user = userRepository.findByUsername(username);
            User associatedUser = userRepository.associatedUser(username);

            userRepository.removeAssociatedUser(user.getId(), username);
            userRepository.removeAssociatedUser(associatedUser.getId(),
                    associatedUser.getUsername());
        } catch (Exception e) {
            return "The user removal failed";
        }
        return "Success";
    }

    private JsonObject genereateAuthOutput(User user) {
        JsonObject output = new JsonObject();
        output.addProperty("token", jwtTokenUtil.generateToken(user.getUsername()));
        output.addProperty("picture", user.getPicture());
        return output;
    }

}

