package com.opensource.OAuth2server.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opensource.OAuth2server.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
