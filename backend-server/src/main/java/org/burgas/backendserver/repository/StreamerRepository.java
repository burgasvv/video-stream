package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {
}
