package com.auction.platform.service;

import java.util.Map;

import com.auction.platform.dto.AuthResponse;

public interface AuthService {
	Map<String, String> login(String email, String password);
}
