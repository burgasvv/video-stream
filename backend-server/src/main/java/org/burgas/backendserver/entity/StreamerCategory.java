package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@SuppressWarnings(value = "unused")
@IdClass(value = StreamerCategoryPK.class)
public class StreamerCategory {

    @Id private Long streamerId;
    @Id private Long categoryId;
    private LocalDateTime addedAt;

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final StreamerCategory streamerCategory;

        public Builder() {
            streamerCategory = new StreamerCategory();
        }

        public Builder streamerId(Long streamerId) {
            this.streamerCategory.streamerId = streamerId;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.streamerCategory.categoryId = categoryId;
            return this;
        }

        public Builder addedAt(LocalDateTime addedAt) {
            this.streamerCategory.addedAt = addedAt;
            return this;
        }

        public StreamerCategory build() {
            return streamerCategory;
        }
    }
}
