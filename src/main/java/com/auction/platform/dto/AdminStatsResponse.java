package com.auction.platform.dto;

public class AdminStatsResponse {

    private long totalUsers;
    private long totalAuctions;
    private long activeAuctions;
    private double platformRevenue;

    public AdminStatsResponse(long totalUsers, long totalAuctions, long activeAuctions, double platformRevenue) {
        this.totalUsers = totalUsers;
        this.totalAuctions = totalAuctions;
        this.activeAuctions = activeAuctions;
        this.platformRevenue = platformRevenue;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalAuctions() {
        return totalAuctions;
    }

    public long getActiveAuctions() {
        return activeAuctions;
    }

    public double getPlatformRevenue() {
        return platformRevenue;
    }
}
