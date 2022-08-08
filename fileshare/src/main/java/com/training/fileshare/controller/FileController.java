package com.training.fileshare.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.fileshare.domain.User;
import com.training.fileshare.dto.TextFileDto;
import com.training.fileshare.service.TextFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

	private final TextFileService textFileService;

	@GetMapping
	public List<TextFileDto> fileList(@AuthenticationPrincipal User user) {
		return textFileService.getAllUserFiles(user);
	}

	@PostMapping
	public Map<String, String> saveFile(MultipartFile file, @AuthenticationPrincipal User author)
			throws IllegalStateException, IOException {

		Map<String, String> result = new HashMap<>();
		result.put("Created file id", textFileService.saveTextFile(file, author).toString());

		return result;
	}

}
