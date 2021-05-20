package com.me.repository;

import com.me.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepositoryRds extends JpaRepository<Image, String> {
}
