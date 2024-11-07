package com.bappi.videoinventorymanagement.model.dto;

import com.bappi.videoinventorymanagement.utils.IAPIErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VideoInfoResponseDto {

    private String code;
    private String title;
    private String description;
    private String videoUrl;
    private String message;
    private IAPIErrorCode errorCode;
}
