package com.training.fileshare.dto;

import java.io.File;

import lombok.Data;

@Data
public class TextFileDto {
	private Long id;
	private String authorName;
	private File file;
}
