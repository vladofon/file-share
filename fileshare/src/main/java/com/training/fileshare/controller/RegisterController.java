package com.training.fileshare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.fileshare.domain.User;
import com.training.fileshare.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RegisterController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> user(@RequestBody User user) {

		if (!userService.addUser(user)) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(HttpStatus.CREATED);
	}
}
