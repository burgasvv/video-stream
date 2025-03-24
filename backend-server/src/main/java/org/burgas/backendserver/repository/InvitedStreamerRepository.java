package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.InvitedStreamer;
import org.burgas.backendserver.entity.InvitedStreamerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitedStreamerRepository extends JpaRepository<InvitedStreamer, InvitedStreamerPK> {
}
