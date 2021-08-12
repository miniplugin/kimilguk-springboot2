package com.edu.springboot2.domain.simple_users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SimpleUsersRepository extends JpaRepository<SimpleUsers, Long> {

    @Query("SELECT p FROM SimpleUsers p ORDER BY p.id DESC")
    List<SimpleUsers> findAllDesc();

}
