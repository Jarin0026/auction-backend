package com.auction.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.platform.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
	
	User  findByEmail(String email);

}
