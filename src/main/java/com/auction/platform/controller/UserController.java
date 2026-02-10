package com.auction.platform.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.config.JwtUtil;
import com.auction.platform.dto.LoginRequest;
import com.auction.platform.entity.User;
import com.auction.platform.service.AuthService;
import com.auction.platform.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		return userService.registerUser(user);
	}
	
	/*
	 * @PostMapping("/login") public Map<String, String> loginUser(@RequestBody
	 * LoginRequest request) {
	 * 
	 * User user = userService.getByEmail(request.getEmail());
	 * 
	 * if (!passwordEncoder.matches( request.getPassword(), user.getPassword())) {
	 * 
	 * throw new RuntimeException("Invalid password"); }
	 * 
	 * String token = jwtUtil.generateToken( user.getEmail(), user.getRole().name()
	 * );
	 * 
	 * Map<String, String> response = new HashMap(); response.put("token", token);
	 * response.put("role", user.getRole().name()); response.put("userId",
	 * String.valueOf(user.getId()));
	 * 
	 * 
	 * return response; }
	 */
	
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public Map<String, String> loginUser(@RequestBody LoginRequest request) {
	    return authService.login(
	        request.getEmail(),
	        request.getPassword()
	    );
	}

	
}
