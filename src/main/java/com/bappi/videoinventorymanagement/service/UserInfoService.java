package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;

import java.util.Optional;

public interface UserInfoService {
    Optional<UserInfo> getById(Long id);
    Boolean addUser(UserInfoRequestDto requestDto);
}
