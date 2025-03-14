package org.burgas.identityserver.repository;

import org.burgas.identityserver.entity.Identity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IdentityRepository extends R2dbcRepository<Identity, Long> {

    Mono<Identity> findIdentityByEmail(String email);
}
