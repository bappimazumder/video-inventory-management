package com.bappi.videoinventorymanagement.controller;

import com.bappi.videoinventorymanagement.model.dto.AuthRequestDto;
import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.service.UserInfoService;
import com.bappi.videoinventorymanagement.service.impl.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bappi.videoinventorymanagement.config.ApiPath.*;

@Slf4j
@RestController
@RequestMapping(API_USER)
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
        this.userInfoService = userInfoService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping(value=API_POST_ADD_USER)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Boolean userRegister(@RequestBody UserInfoRequestDto requestDto){
        return userInfoService.addUser(requestDto);
    }

    @PostMapping(value = API_POST_USER_AUTHENTICATE)
    public String authenticateAndGetToken(@RequestBody AuthRequestDto requestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                requestDto.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtTokenService.generateToken(requestDto.getUsername());
        }else {
            throw new UsernameNotFoundException("Invalid user Request");
        }
    }

}
