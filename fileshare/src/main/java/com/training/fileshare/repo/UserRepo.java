package com.training.fileshare.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.fileshare.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
