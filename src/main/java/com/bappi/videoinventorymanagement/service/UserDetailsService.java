package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.entity.UserDetails;

import java.util.Optional;

public interface UserDetailsService
{

    Optional<UserDetails> getById(Long id);
}
