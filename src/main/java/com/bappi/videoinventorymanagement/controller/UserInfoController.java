package com.bappi.videoinventorymanagement.controller;

import com.bappi.videoinventorymanagement.model.dto.AuthRequestDto;
import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.service.UserInfoService;
import com.bappi.videoinventorymanagement.service.impl.JwtTokenService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.CustomException;
import com.bappi.videoinventorymanagement.utils.ServiceExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // User Registration API
    @PostMapping(value=API_POST_ADD_USER)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<?> userRegister(@RequestBody UserInfoRequestDto requestDto){
        log.info("Call Start userRegister method for user {}",requestDto.getEmail());
        ServiceExceptionHandler<String> dataHandler = () ->  userInfoService.addUser(requestDto);
        return new ResponseEntity<String>(dataHandler.executeHandler(), HttpStatus.CREATED);
    }

    // GetToken API
    @PostMapping(value = API_POST_USER_GET_TOKEN)
    public String authenticateAndGetToken(@RequestBody AuthRequestDto requestDto){

        if (requestDto.getUsername().isEmpty()) {
            throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        if (requestDto.getPassword().isEmpty()) {
            throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }

        log.info("Call Start authenticateAndGetToken method for user {}",requestDto.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                requestDto.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtTokenService.generateToken(requestDto.getUsername());
        }else {
            log.error("User Not Found");
            throw new CustomException(APIErrorCode.NO_RECORD_FOUND,HttpStatus.OK);
        }
    }

}
