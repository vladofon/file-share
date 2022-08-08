package com.training.fileshare.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.fileshare.domain.TextFile;
import com.training.fileshare.domain.User;
import com.training.fileshare.dto.TextFileDto;
import com.training.fileshare.repo.TextFileRepo;
import com.training.fileshare.service.mapper.TextFileDtoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextFileService {

	private final TextFileRepo textFileRepo;
	private final TextFileDtoMapper mapper;
	private final FileManager fileManager;

	public List<TextFileDto> getUserFiles(User user) {
		return mapper.mapAll(textFileRepo.findByAuthor(user));
	}

	public List<TextFileDto> getFilesSharedWith(User user) {
		return mapper.mapAll(textFileRepo.findByConsumer(user));
	}

	public List<TextFileDto> getAllUserFiles(User user) {
		List<TextFileDto> userFiles = getUserFiles(user);
		List<TextFileDto> filesSharedWithUser = getFilesSharedWith(user);

		return Stream.concat(userFiles.stream(), filesSharedWithUser.stream()).collect(Collectors.toList());
	}

	public Long saveTextFile(MultipartFile file, User author) throws IllegalStateException, IOException {
		fileManager.saveOnDisk(file);

		TextFile textFile = new TextFile();
		textFile.setFilename(file.getOriginalFilename());
		textFile.setAuthor(author);

		return textFileRepo.save(textFile).getId();
	}
}
