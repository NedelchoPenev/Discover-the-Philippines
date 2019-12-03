package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.ThingsToDo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThingsToDoRepository extends JpaRepository<ThingsToDo, String> {

  Page<ThingsToDo> findAll(Pageable pageable);

  Page<ThingsToDo> findByProvince(String province, Pageable pageable);

  Page<ThingsToDo> findByNameContains(String name, Pageable pageable);

  @Query("SELECT province FROM ThingsToDo")
  List<String> getAllProvinces();

  Optional<ThingsToDo> findByName(String name);
}
