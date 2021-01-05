package com.joaomariajaneiro.datejar.service;

import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int associateUser(String username, String partnerUsername) {
        return userRepository.associateUser(username, partnerUsername);
    }

    public User associatedUser(String username) {
        return userRepository.associatedUser(username);
    }

    public int removeAssociatedUser(Long id, String username) {
        return userRepository.removeAssociatedUser(id, username);
    }

    public int save(User user) throws ResponseStatusException {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            String trim = e.getCause().getLocalizedMessage().split("Detail:")[1].trim();
            trim = trim.substring(trim.length() - 16);
            if (trim.contains("already exists.")) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username and/or " +
                        "email already taken");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error " +
                        "occurred while creating your account, please try again");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error " +
                    "occurred while creating your account, please try again");
        }
    }
}
