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

		if (FilenameUtils.getExtension(file.getOriginalFilename()) != "txt") {
			throw new IllegalArgumentException("Wrong file extention! Must be [.txt]");
		}

		String path = request.getServletContext().getRealPath(uploadDir);

		if (!new File(path).exists()) {
			new File(path).mkdir();
		}

		String filename = file.getOriginalFilename();
		String filePath = path + filename;

		File destination = new File(filePath);
		file.transferTo(destination);
	}

	public File loadFormDisk(String filename) {
		String path = request.getServletContext().getRealPath(uploadDir);
		String filePath = path + filename;

		File file = new File(filePath);

		if (!file.exists()) {
			throw new IllegalArgumentException("File with name [" + filename + "] does not exist!");
		}

		return file;
	}
}
