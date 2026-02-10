package com.auction.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.entity.User;
import com.auction.platform.repository.UserRepository;

@RestController()
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@PutMapping("/{id}/block")
	public String blockUser(@PathVariable long id) {
		User user = userRepository.findById(id).orElseThrow();
		
		user.setEnabled(false);
		userRepository.save(user);
		return "Blocked User";
		
	}
	
	@PutMapping("/{id}/unblock")
	public String unblockUser(@PathVariable long id) {
		User user = userRepository.findById(id).orElseThrow();
		user.setEnabled(true);
		userRepository.save(user);
		return "User unblock";
	}
	
	
	
}
