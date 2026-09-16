package COMP3011.assignment1.controller;

import COMP3011.assignment1.model.UptimeResponse;
import COMP3011.assignment1.service.ServerStateService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ServerStateService serverStateService;
    private final ApplicationContext applicationContext;
    private final AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    public AdminController(ServerStateService serverStateService, ApplicationContext applicationContext) {
        this.serverStateService = serverStateService;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/uptime")
    public UptimeResponse getUptime() {
        Instant start = serverStateService.getServerStartTime();
        Instant now = Instant.now();
        double uptimeSeconds = Duration.between(start, now).toMillis() / 1000.0;
        return new UptimeResponse(start.toString(), now.toString(), uptimeSeconds);
    }

    @PostMapping("/shutdown")
    public ResponseEntity<Map<String, String>> shutdown() {
        if (!shutdownInProgress.compareAndSet(false, true)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Graceful shutdown is already in progress."));
        }

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            System.exit(SpringApplication.exit(applicationContext, () -> 0));
        }).start();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("message", "Graceful shutdown requested."));
    }
}