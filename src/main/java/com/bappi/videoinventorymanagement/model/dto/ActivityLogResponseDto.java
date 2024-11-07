package com.bappi.videoinventorymanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogResponseDto {

    private String userId;
    private String videoId;
    private Timestamp activityAt;
    private String actionType;
}
