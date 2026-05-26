package com.devopsflowai.config;

import com.devopsflowai.entity.*;
import com.devopsflowai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final TaskRepository tasks;
    private final DeploymentRepository deployments;
    private final ApiEndpointRepository endpoints;
    private final AlertRepository alerts;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;
        User admin = users.save(User.builder().name("Aarav Admin").email("admin@devopsflow.ai").password(encoder.encode("admin123")).role(Role.ADMIN).build());
        User lead = users.save(User.builder().name("Team Lead").email("lead@devopsflow.ai").password(encoder.encode("lead123")).role(Role.TEAM_LEAD).build());
        User dev = users.save(User.builder().name("Demo Developer").email("dev@devopsflow.ai").password(encoder.encode("dev123")).role(Role.DEVELOPER).build());

        tasks.save(TaskItem.builder().title("Fix deployment rollback script").description("Rollback job fails on database migration release").assignedTo(dev).status(TaskStatus.IN_PROGRESS).priority(Priority.CRITICAL).deadline(LocalDate.now().plusDays(2)).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
        tasks.save(TaskItem.builder().title("Add API latency dashboard").description("Show p95 latency for monitored services").assignedTo(lead).status(TaskStatus.TESTING).priority(Priority.HIGH).deadline(LocalDate.now().plusDays(5)).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
        tasks.save(TaskItem.builder().title("Write release checklist").description("Checklist for staging to production release").assignedTo(admin).status(TaskStatus.COMPLETED).priority(Priority.MEDIUM).deadline(LocalDate.now().minusDays(1)).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());

        deployments.save(Deployment.builder().version("v1.4.0").environment("production").status(DeploymentStatus.SUCCESS).deployedBy(admin).releaseNotes("Stable task dashboard release").riskScore(21).deployedAt(LocalDateTime.now().minusDays(2)).build());
        deployments.save(Deployment.builder().version("v1.5.0-hotfix").environment("staging").status(DeploymentStatus.FAILED).deployedBy(lead).releaseNotes("Urgent auth hotfix with database migration").riskScore(91).deployedAt(LocalDateTime.now().minusHours(5)).build());

        endpoints.save(ApiEndpoint.builder().name("Local backend health").url("http://localhost:8080/actuator/health").active(false).expectedLatencyMs(350).build());
        endpoints.save(ApiEndpoint.builder().name("Public GitHub API").url("https://api.github.com").active(false).expectedLatencyMs(1000).build());

        alerts.save(Alert.builder().type("DEPLOYMENT_RISK").message("v1.5.0-hotfix risk score is high because it includes auth and migration changes").severity(AlertSeverity.CRITICAL).resolved(false).createdAt(LocalDateTime.now()).build());
    }
}
