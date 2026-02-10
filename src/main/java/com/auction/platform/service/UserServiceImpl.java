package com.auction.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Role;
import com.auction.platform.entity.User;
import com.auction.platform.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public User registerUser(User user) {

		if (user.getRole() == null) {
			user.setRole(Role.BUYER); // default role
		}

		user.setPassword(encoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public User LoginUser(String email, String password) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new RuntimeException("User not found");
		}

		if (!encoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid password");
		}

		return user;
	}

	@Override
	public User getByEmail(String email) {
		User user = userRepository.findByEmail(email);

		if (user == null)
			throw new RuntimeException("User not found");

		return user;

	}

}
