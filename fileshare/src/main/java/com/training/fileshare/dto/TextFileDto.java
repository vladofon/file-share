package com.training.fileshare.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TextFileDto {
	private Long id;
	private String authorName;
	private MultipartFile file;
}
