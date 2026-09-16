package COMP3011.assignment1.model;

public class UptimeResponse {
    private final String utcServerStart;
    private final String utcNow;
    private final double serverUptimeSeconds;

    public UptimeResponse(String utcServerStart, String utcNow, double serverUptimeSeconds) {
        this.utcServerStart = utcServerStart;
        this.utcNow = utcNow;
        this.serverUptimeSeconds = serverUptimeSeconds;
    }

    public String getUtcServerStart() { return utcServerStart; }
    public String getUtcNow() { return utcNow; }
    public double getServerUptimeSeconds() { return serverUptimeSeconds; }
}