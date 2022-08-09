package com.training.fileshare.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.fileshare.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
