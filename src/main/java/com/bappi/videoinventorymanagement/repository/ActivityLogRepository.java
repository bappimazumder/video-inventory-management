package com.bappi.videoinventorymanagement.repository;

import com.bappi.videoinventorymanagement.model.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
