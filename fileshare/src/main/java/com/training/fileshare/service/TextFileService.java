package com.training.fileshare.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.fileshare.domain.TextFile;
import com.training.fileshare.domain.User;
import com.training.fileshare.dto.TextFileDto;
import com.training.fileshare.repo.TextFileRepo;
import com.training.fileshare.repo.UserRepo;
import com.training.fileshare.service.mapper.TextFileDtoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextFileService {

	private final TextFileRepo textFileRepo;
	private final UserRepo userRepo;
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

	public Resource download(Long id, User owner) {
		Optional<TextFile> document = textFileRepo.findById(id);

		if (document.isEmpty()) {
			throw new IllegalArgumentException("File with id [" + id + "] does not exist");
		}

		if (!getAllUserFilesDetails(owner).contains(document.get())) {
			throw new IllegalArgumentException(
					"User [" + owner.getUsername() + "] has no access to file [id:" + document.get().getId() + "]");
		}

		File toDownload = fileManager.loadFormDisk(document.get().getFilename());
		return getFileResource(toDownload);
	}

	public void shareFile(Long id, String email, User author) {
		Optional<TextFile> document = textFileRepo.findById(id);
		Optional<User> user = userRepo.findByEmail(email);

		if (document.isEmpty()) {
			throw new IllegalArgumentException("File to share not found [id:" + id + "]");
		}

		if (user.isEmpty()) {
			throw new IllegalArgumentException("No User found for file sharing [email:" + email + "]");
		}

		if (!document.get().getAuthor().equals(author)) {
			throw new IllegalArgumentException("User [email:" + author.getUsername() + "] is not File owner [email:"
					+ document.get().getAuthor().getUsername() + "]");
		}

		// user.get().getRecievedFiles().add(document.get());
		document.get().getConsumers().add(user.get());
		textFileRepo.save(document.get());
		// userRepo.save(user.get());
	}

	private Resource getFileResource(File file) {

		Resource resource = null;
		try {
			resource = new UrlResource(file.toURI());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return resource;
	}

	private List<TextFile> getAllUserFilesDetails(User user) {
		List<TextFile> filesData = new ArrayList<>();

		filesData.addAll(textFileRepo.findByAuthor(user));
		filesData.addAll(textFileRepo.findByConsumer(user));

		return filesData;
	}
}
