package com.training.fileshare.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileManager {

	@Value("${server.upload-directory}")
	private String uploadDir;

	private final HttpServletRequest request;

	public void saveOnDisk(MultipartFile file) throws IllegalStateException, IOException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (!extension.equals("txt")) {
			throw new IllegalArgumentException("Wrong file extention! Must be [.txt] | was: [" + extension + "]");
		}

		if (!(new File(uploadDir).exists())) {
			new File(uploadDir).mkdir();
		}

		String filename = file.getOriginalFilename();
		String filePath = uploadDir + filename;

		File destination = new File(filePath);
		file.transferTo(destination);
	}

	public File loadFormDisk(String filename) {
		String filePath = uploadDir + filename;

		File file = new File(filePath);

		if (!file.exists()) {
			throw new IllegalArgumentException("File with name [" + filename + "] does not exist!");
		}

		return file;
	}
}
