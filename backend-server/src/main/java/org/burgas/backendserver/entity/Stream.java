package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings(value = "unused")
public final class Stream {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Long streamerId;
    private Long categoryId;
    private LocalDateTime started;
    private LocalDateTime ended;
    private Boolean isLive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getEnded() {
        return ended;
    }

    public void setEnded(LocalDateTime ended) {
        this.ended = ended;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean alive) {
        isLive = alive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Stream stream;

        public Builder() {
            stream = new Stream();
        }

        public Builder id(Long id) {
            this.stream.id = id;
            return this;
        }

        public Builder name(String name) {
            this.stream.name = name;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.stream.streamerId = streamerId;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.stream.categoryId = categoryId;
            return this;
        }

        public Builder started(LocalDateTime started) {
            this.stream.started = started;
            return this;
        }

        public Builder ended(LocalDateTime ended) {
            this.stream.ended = ended;
            return this;
        }

        public Builder isLive(Boolean isLive) {
            this.stream.isLive = isLive;
            return this;
        }

        public Stream build() {
            return stream;
        }
    }
}
