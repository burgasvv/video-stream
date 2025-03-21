package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select c.* from category c join streamer_category sc on c.id = sc.category_id
                                        where sc.streamer_id = :streamerId
                    """
    )
    List<Category> findCategoriesByStreamerId(Long streamerId);
}
