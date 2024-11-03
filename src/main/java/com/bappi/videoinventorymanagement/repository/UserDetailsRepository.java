package com.bappi.videoinventorymanagement.repository;

import com.bappi.videoinventorymanagement.model.entity.UserDetails;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    UserDetails findByCode(String code);
}
