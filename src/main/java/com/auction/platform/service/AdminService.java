package com.auction.platform.service;

import java.util.Map;

import com.auction.platform.dto.AdminStatsResponse;

public interface AdminService {
    AdminStatsResponse getStats();
    Map<Integer, Double> getMonthlyRevenue();

    
   
}
