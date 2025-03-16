package org.burgas.streamservice.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("unused")
public final class IdentityStreamerTokenPK {

    private Long identityId;
    private Long streamerId;

    public Long getIdentityId() {
        return identityId;
    }

    public Long getStreamerId() {
        return streamerId;
    }
}
