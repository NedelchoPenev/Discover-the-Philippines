package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
  List<Image> findByProvince(String province);

  List<Image> findByPlace(String place);
}
