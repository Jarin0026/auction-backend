package com.auction.platform.dto;

public class AuthResponse {

	private String token;
    private String role;
    private long userId;

    public AuthResponse(String token, String role, long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
    
    
	
}
