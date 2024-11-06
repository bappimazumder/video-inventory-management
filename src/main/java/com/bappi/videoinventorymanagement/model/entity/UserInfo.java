package com.bappi.videoinventorymanagement.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.bappi.videoinventorymanagement.config.UserInfoDBConstant.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = USER_INFO_TABLE)
public class UserInfo {

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = EMAIL,unique=true)
    private String email;

    @Column(name = FULL_NAME)
    private String fullName;

    @Column(name = PASSWORD)
    private String password;

    @Column(name = ROLES)
    private String roles;

    @OneToMany(mappedBy = "assignedToUser")
    private List<VideoInfo> videoInfos;

}
