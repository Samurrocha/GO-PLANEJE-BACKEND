package com.pegasus.goplaneje.repositories;

import com.pegasus.goplaneje.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.lang.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT u.email FROM users u WHERE u.email IN :emails", nativeQuery = true)
    List<String> findEmailsInList(@Param("emails") List<String> emails);

}
