package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("unused")
public final class StreamerCategoryPK {

    private Long streamerId;
    private Long categoryId;

    public Long getStreamerId() {
        return streamerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
