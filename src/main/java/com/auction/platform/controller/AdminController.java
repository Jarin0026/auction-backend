package com.auction.platform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.dto.AdminStatsResponse;
import com.auction.platform.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/stats")
	public AdminStatsResponse stats() {
		return adminService.getStats();
	}
	
	@GetMapping("/revenue/monthly")
	public Map<Integer, Double> monthlyRevenue() {
	    return adminService.getMonthlyRevenue();
	}
	
	

	
}
