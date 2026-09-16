package COMP3011.assignment1.controller;

import COMP3011.assignment1.model.GlobalStatsResponse;
import COMP3011.assignment1.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public GlobalStatsResponse getStats() {
        return new GlobalStatsResponse(statsService.getInputTokens(), statsService.getOutputTokens());
    }
}