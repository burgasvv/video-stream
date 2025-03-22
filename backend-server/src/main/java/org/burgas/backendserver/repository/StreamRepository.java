package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {

    List<Stream> findStreamsByStreamerId(Long streamerId);
}
