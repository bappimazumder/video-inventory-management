package com.bappi.videoinventorymanagement.utils.mapper;

import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import org.mapstruct.Mapper;

@Mapper
public interface UserInfoObjectMapper {
    UserInfo map(UserInfoRequestDto requestDto);
}
