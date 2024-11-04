package com.bappi.videoinventorymanagement.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoInfoRequestDto {
    private String title;
    private String description;
    private Long userId;

}
