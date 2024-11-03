package com.bappi.videoinventorymanagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static com.bappi.videoinventorymanagement.config.ActivityLogDBConstant.*;

@Getter
@Setter
@Entity
@Table(name = ACTIVITY_LOG_TABLE)
public class ActivityLog {

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = USER_ID)
    private UserDetails user;

    @ManyToOne
    @JoinColumn(name = VIDEO_ID)
    private VideoInfo videoInfo;

    @Column(name = ACTIVITY_AT)
    private Timestamp activityAt;

    @Column(name = ACTION_TYPE)
    private String actionType;
}
