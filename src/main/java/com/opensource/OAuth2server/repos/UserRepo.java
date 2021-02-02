package com.opensource.OAuth2server.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opensource.OAuth2server.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
