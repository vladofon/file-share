package com.training.fileshare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.training.fileshare.domain.TextFile;
import com.training.fileshare.domain.User;

public interface TextFileRepo extends JpaRepository<TextFile, Long> {

	List<TextFile> findByAuthor(User author);

	@Query("SELECT U.recievedFiles FROM com.training.fileshare.domain.User U WHERE U = :consumer")
	List<TextFile> findByConsumer(User consumer);

}
