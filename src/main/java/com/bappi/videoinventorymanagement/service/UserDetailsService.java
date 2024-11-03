package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.entity.UserDetails;

public interface UserDetailsService
{

    UserDetails getByCode(String code);
}
