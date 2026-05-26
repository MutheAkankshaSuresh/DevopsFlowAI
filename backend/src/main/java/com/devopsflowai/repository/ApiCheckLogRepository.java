package com.devopsflowai.repository;

import com.devopsflowai.entity.ApiCheckLog;
import com.devopsflowai.entity.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiCheckLogRepository extends JpaRepository<ApiCheckLog, Long> {
    List<ApiCheckLog> findTop20ByOrderByCheckedAtDesc();
    List<ApiCheckLog> findTop10ByEndpointOrderByCheckedAtDesc(ApiEndpoint endpoint);
}
