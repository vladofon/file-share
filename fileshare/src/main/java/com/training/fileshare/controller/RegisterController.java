package com.training.fileshare.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
	public void user(HttpServletResponse response, User user) throws IOException {

		if (!userService.addUser(user)) {
			response.setStatus(400);
			response.sendRedirect("static/register.html");
		}

		response.setStatus(201);
		response.sendRedirect("static/login.html");
	}
}
