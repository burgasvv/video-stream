package org.burgas.identityserver.repository;

import org.burgas.identityserver.entity.Authority;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends R2dbcRepository<Authority, Long> {
}
