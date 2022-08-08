package com.training.fileshare.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.training.fileshare.domain.TextFile;
import com.training.fileshare.dto.TextFileDto;
import com.training.fileshare.service.FileManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TextFileDtoMapper {

	private final FileManager fileManager;

	public TextFileDto map(TextFile textFile) {
		TextFileDto dto = new TextFileDto();

		dto.setId(textFile.getId());
		dto.setAuthorName(textFile.getAuthor().getUsername());
		dto.setFile(fileManager.loadFormDisk(textFile.getFilename()));

		return dto;
	}

	public List<TextFileDto> mapAll(List<TextFile> textFiles) {
		return textFiles.stream().map(this::map).collect(Collectors.toList());
	}
}
