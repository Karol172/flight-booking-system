package com.karol.app.repository;

import com.karol.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByLastName (String lastName);

    boolean existsByEmail (String email);

    Optional<User> findByEmail (String email);

}
