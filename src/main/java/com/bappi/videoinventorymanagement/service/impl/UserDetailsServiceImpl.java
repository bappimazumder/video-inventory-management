package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.model.entity.UserDetails;
import com.bappi.videoinventorymanagement.repository.UserDetailsRepository;
import com.bappi.videoinventorymanagement.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserDetails> getById(Long id) {
        return repository.findById(id);
    }
}
