package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findInvitationsBySenderId(Long senderId);

    List<Invitation> findInvitationsByReceiverId(Long receiverId);
}
