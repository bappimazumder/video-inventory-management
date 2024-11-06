package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import com.bappi.videoinventorymanagement.repository.UserInfoRepository;
import com.bappi.videoinventorymanagement.service.UserInfoService;
import com.bappi.videoinventorymanagement.utils.mapper.UserInfoObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bappi.videoinventorymanagement.config.Constant.MSG_USER_CREATED_SUCCESSFULLY;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final PasswordEncoder passwordEncoder;

    private final UserInfoObjectMapper objectMapper;

    private final UserInfoRepository repository;

    public UserInfoServiceImpl(PasswordEncoder passwordEncoder, UserInfoRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.objectMapper = Mappers.getMapper(UserInfoObjectMapper.class);
    }

    public Optional<UserInfo> getById(Long id) {
        return repository.findById(id);
    }

    public String addUser(UserInfoRequestDto requestDto){
        UserInfo userInfo = objectMapper.map(requestDto);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return MSG_USER_CREATED_SUCCESSFULLY;
    }
}
