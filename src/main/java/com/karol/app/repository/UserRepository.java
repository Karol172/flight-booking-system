package com.karol.app.repository;

import com.karol.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByLastName (String lastName);

    Boolean existsByMail (String mail);
}

