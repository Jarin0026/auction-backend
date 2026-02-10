package com.auction.platform.service;

import com.auction.platform.entity.User;

public interface UserService  {

	User registerUser(User user);
	User LoginUser(String email, String password);
	 User getByEmail(String email);

	
}
