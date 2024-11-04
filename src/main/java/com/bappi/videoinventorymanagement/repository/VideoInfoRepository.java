package com.bappi.videoinventorymanagement.repository;

import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoInfoRepository extends JpaRepository<VideoInfo, Long> {

    List<VideoInfo> findByAssignedToUserId(Long userId);

}
