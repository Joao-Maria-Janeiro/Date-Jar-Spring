package com.joaomariajaneiro.datejar.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.ConfirmationToken;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import com.joaomariajaneiro.datejar.service.ConfirmationTokenService;
import com.joaomariajaneiro.datejar.service.UserService;
import com.joaomariajaneiro.datejar.utils.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

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
            user = userService.findByUsername(jsonNode.get("username").asText());
        } catch (Exception e) {
            throw new AuthenticationException();
        }

        if (!user.isEnabled()) {
            throw new AuthenticationException();
        }


        return genereateAuthOutput(user).toString();
    }

    @PostMapping(value = "/create")
    public String signup(@RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        if (!emailIsValid(jsonNode.get("email").asText())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Email");
        }

        User user;
        try {
            user = new User(
                    jsonNode.get("username").asText(),
                    jsonNode.get("email").asText(),
                    passwordEncoder.encode(jsonNode.get("password").asText()),
                    jsonNode.get("picture").asText()
            );
            userService.save(user);


            ConfirmationToken confirmationToken = new ConfirmationToken();
            confirmationTokenService.save(confirmationToken, user.getUsername());
            SendEmail sendEmail = new SendEmail(user.getEmail(), "localhost");
            sendEmail.sendMail("Confirm your account on Me2", "Click the following link to " +
                    "confirm your account:\n" + System.getenv("backend_url") + "users/confirm/" + confirmationToken.getToken() + "/" + user.getUsername());
        } catch (ResponseStatusException e) {
            throw e;
        }

        return "success";
    }

    @GetMapping(value = "/confirm/{confirmationToken}/{username}")
    public String confirmUserAccount(@PathVariable String confirmationToken,
                                     @PathVariable String username) {
        try {
            ConfirmationToken token = confirmationTokenService.findByUser(username);
            if (token.getToken().equals(confirmationToken)) {
                userService.confirmUser(username);
                return "Account confirmed successfully";
            }
        } catch (Exception e) {
            return "There was an error confirming your account, is this a valid token";
        }
        return "There was an error confirming your account, is this a valid token";
    }


    @PostMapping(value = "/associate")
    public String associateUser(@RequestHeader Map<String, String> headers,
                                @RequestBody String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);


        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));


            String partnerEmail = userService.findByUsername(jsonNode.get(
                    "partner_username").asText()).getEmail();
            String email = userService.findByUsername(username).getEmail();
            SendEmail sendEmail = new SendEmail(partnerEmail, "localhost");

            sendEmail.sendMail(username + " wants to be your friend on Me2",
                    System.getenv("backend_url") + "users/confirm-friend/" + email + "/" + partnerEmail);
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

            userService.associateUser(email,
                    partnerEmail);
            userService.associateUser(partnerEmail,
                    email);

            SendEmail sendEmail = new SendEmail(email, "localhost");

            User user = userService.findByEmail(partnerEmail);

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

            return userService.associatedUser(username).getUsername();
        } catch (Exception e) {
            return "There was an error retrieving your friend";
        }
    }

    @PostMapping(value = "/remove-friend")
    public String removeAssociateUser(@RequestHeader Map<String, String> headers) throws JsonProcessingException {
        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            User user = userService.findByUsername(username);
            User associatedUser = userService.associatedUser(username);

            userService.removeAssociatedUser(user.getId(), username);
            userService.removeAssociatedUser(associatedUser.getId(),
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

    public static boolean emailIsValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}

