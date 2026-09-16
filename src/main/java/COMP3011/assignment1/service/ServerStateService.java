package COMP3011.assignment1.service;

import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class ServerStateService {
    private final Instant serverStartTime = Instant.now();

    public Instant getServerStartTime() {
        return serverStartTime;
    }
}