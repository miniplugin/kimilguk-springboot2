package com.edu.springboot2.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManyFileRepository extends JpaRepository<ManyFile, Long> {
}
