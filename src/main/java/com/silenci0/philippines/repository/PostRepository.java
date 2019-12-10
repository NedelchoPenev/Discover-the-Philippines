package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

  Page<Post> findAll(Pageable pageable);

  Page<Post> findByTitleContains(String keyword, Pageable pageable);

}
