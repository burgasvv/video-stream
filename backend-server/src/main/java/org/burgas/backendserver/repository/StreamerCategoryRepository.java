package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.StreamerCategory;
import org.burgas.backendserver.entity.StreamerCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamerCategoryRepository extends JpaRepository<StreamerCategory, StreamerCategoryPK> {

    Boolean existsStreamerCategoryByStreamerIdAndCategoryId(Long streamerId, Long categoryId);

    StreamerCategory findStreamerCategoryByStreamerIdAndCategoryId(Long streamerId, Long categoryId);
}
