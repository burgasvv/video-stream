package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StreamerCategoryPK that = (StreamerCategoryPK) o;
        return Objects.equals(streamerId, that.streamerId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamerId, categoryId);
    }
}
