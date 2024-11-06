package com.bappi.videoinventorymanagement.model.dto;

import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import lombok.Getter;

@Getter
public class VideoInfoResponseDto {

    private String code;
    private String title;
    private String description;
    private String videoUrl;
    private UserInfo assignedToUser;
}
