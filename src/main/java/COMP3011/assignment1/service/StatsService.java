package COMP3011.assignment1.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatsService {
    // AtomicLong is thread-safe for concurrent increments without needing manual locks -
    // important since multiple transcription requests could update this at the same time
    private final AtomicLong inputTokens = new AtomicLong(0);
    private final AtomicLong outputTokens = new AtomicLong(0);

    public long getInputTokens() { return inputTokens.get(); }
    public long getOutputTokens() { return outputTokens.get(); }

    public void addUsage(long input, long output) {
        inputTokens.addAndGet(input);
        outputTokens.addAndGet(output);
    }
}