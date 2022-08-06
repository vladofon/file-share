package com.training.fileshare.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.fileshare.domain.User;
import com.training.fileshare.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);

		if (user == null) {
			throw new BadCredentialsException("User with email [" + email + "] not found!");
		}

		return user;
	}

	public boolean addUser(User user) {
		boolean hasEmptyFields = user == null || user.getEmail().isEmpty() || user.getPassword().isEmpty();

		if (hasEmptyFields) {
			return false;
		}

		User userFromDb = userRepo.findByEmail(user.getEmail());

		boolean alreadyRegistered = userFromDb != null;

		if (alreadyRegistered) {
			return false;
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);

		return true;
	}

}
