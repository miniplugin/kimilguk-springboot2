package com.edu.springboot2.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManyFileRepository extends JpaRepository<ManyFile, Long> {

    @Query(value = "SELECT * FROM many_file p where p.posts_id = :posts_id ORDER BY p.id DESC", nativeQuery = true)
    ManyFile fileAllDesc(@Param("posts_id") Long id);
}
