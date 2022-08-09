package com.training.fileshare.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.fileshare.domain.User;
import com.training.fileshare.dto.TextFileDto;
import com.training.fileshare.service.TextFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

	private final TextFileService textFileService;

	@GetMapping("/file")
	public List<TextFileDto> fileList(@AuthenticationPrincipal User user) {
		return textFileService.getAllUserFiles(user);
	}

	@PostMapping("/file")
	public Map<String, String> saveFile(MultipartFile file, @AuthenticationPrincipal User author)
			throws IllegalStateException, IOException {

		Map<String, String> result = new HashMap<>();
		result.put("Created file id", textFileService.saveTextFile(file, author).toString());

		return result;
	}

	@GetMapping("/file/{id}")
	public ResponseEntity downloadFile(@PathVariable Long id, @AuthenticationPrincipal User owner) {
		Resource resource = textFileService.download(id, owner);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PostMapping("/share")
	public Map<String, String> shareFile(@RequestParam Long fileId, @RequestParam String email,
			@AuthenticationPrincipal User author) {
		textFileService.shareFile(fileId, email, author);

		Map<String, String> shareInfo = new HashMap<>();
		shareInfo.put("File id:", fileId.toString());
		shareInfo.put("Reciever:", email);
		shareInfo.put("Author:", author.getUsername());

		return shareInfo;
	}

}
