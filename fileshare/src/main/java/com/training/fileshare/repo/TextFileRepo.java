package com.training.fileshare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.fileshare.domain.TextFile;
import com.training.fileshare.domain.User;

public interface TextFileRepo extends JpaRepository<TextFile, Long> {

	List<TextFile> findByAuthor(User author);

}
