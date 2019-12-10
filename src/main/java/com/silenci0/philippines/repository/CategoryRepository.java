package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<PostCategory, String> {

  Page<PostCategory> findAll(Pageable pageable);

  Page<PostCategory> findById(String id, Pageable pageable);
}
