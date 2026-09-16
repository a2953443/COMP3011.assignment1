package COMP3011.assignment1.controller;

import COMP3011.assignment1.model.GlobalStatsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
public class StatsController {

    @GetMapping("/stats")
    public GlobalStatsResponse getStats() {
        return new GlobalStatsResponse(0, 0);
    }
}
