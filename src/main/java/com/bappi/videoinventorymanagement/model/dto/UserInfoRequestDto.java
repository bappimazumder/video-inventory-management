package com.bappi.videoinventorymanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoRequestDto {
    private String email;
    private String fullName;
    private String password;
    private String roles;
}
