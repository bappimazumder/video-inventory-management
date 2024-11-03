package com.bappi.videoinventorymanagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.bappi.videoinventorymanagement.config.UserDetailsDBConstant.*;

@Getter
@Setter
@Entity
@Table(name = USER_DETAILS_TABLE)
public class UserDetails {

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = CODE,unique=true)
    private String code;

    @OneToMany(mappedBy = "assignedToUser")
    private List<VideoInfo> videoInfos;

}
