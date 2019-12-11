package com.silenci0.philippines.repository;

import com.silenci0.philippines.domain.entities.Role;
import com.silenci0.philippines.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findDistinctByUsernameNot(String username, Pageable pageable);

    Page<User> findDistinctByUsernameStartingWithAndUsernameNot(String username, String usernameNot,Pageable pageable);

    void deleteById(String id);

    List<User> findAllByAuthorities(Role role);
}
