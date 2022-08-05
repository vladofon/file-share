package com.training.fileshare.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.training.fileshare.domain.User;
import com.training.fileshare.repo.UserRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);

		if (user == null) {
			throw new BadCredentialsException("User not found!");
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

		userRepo.save(user);

		return true;
	}

}
