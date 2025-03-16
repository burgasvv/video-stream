package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findVideoByName(String name);

    List<Video> findVideosByCategoryId(Long categoryId);
}
