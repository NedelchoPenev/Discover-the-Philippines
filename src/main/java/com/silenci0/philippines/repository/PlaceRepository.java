package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {

  Optional<Place> findByName(String name);

  Page<Place> findAll(Pageable pageable);

  Page<Place> findByNameStartingWith(String name, Pageable pageable);
}
