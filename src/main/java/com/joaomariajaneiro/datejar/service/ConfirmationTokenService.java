package com.joaomariajaneiro.datejar.service;

import com.joaomariajaneiro.datejar.model.ConfirmationToken;
import com.joaomariajaneiro.datejar.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken findByUser(String username) {
        return confirmationTokenRepository.findByUser(username);
    }

    public int save(ConfirmationToken confirmationToken, String username) {
        return confirmationTokenRepository.save(confirmationToken, username);
    }
}
