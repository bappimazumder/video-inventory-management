package com.bappi.videoinventorymanagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.bappi.videoinventorymanagement.config.VideoInfoDBConstant.*;

@Getter
@Setter
@Entity
@Table(name = VIDEO_INFO_TABLE)
public class VideoInfo {

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = CODE,unique=true)
    private String code;

    @Column(name = TITLE)
    private String title;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = VIDEO_URL)
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = ASSIGNED_TO_USER_ID)
    private UserDetails assignedToUser;
}
