package org.burgas.videoservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public final class Video {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Long categoryId;
    private Long streamerId;
    private String description;
    private String contentType;
    private Long size;
    private String format;
    private byte[] data;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public Long getCategoryId() {
        return categoryId;
    }

    @SuppressWarnings("unused")
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @SuppressWarnings("unused")
    public Long getStreamerId() {
        return streamerId;
    }

    @SuppressWarnings("unused")
    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unused")
    public String getContentType() {
        return contentType;
    }

    @SuppressWarnings("unused")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @SuppressWarnings("unused")
    public Long getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public void setSize(Long size) {
        this.size = size;
    }

    @SuppressWarnings("unused")
    public String getFormat() {
        return format;
    }

    @SuppressWarnings("unused")
    public void setFormat(String format) {
        this.format = format;
    }

    @SuppressWarnings("unused")
    public byte[] getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public void setData(byte[] data) {
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Video video;

        public Builder() {
            video = new Video();
        }

        public Builder id(Long id) {
            this.video.id = id;
            return this;
        }

        public Builder name(String name) {
            this.video.name = name;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.video.categoryId = categoryId;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.video.streamerId = streamerId;
            return this;
        }

        public Builder description(String description) {
            this.video.description = description;
            return this;
        }

        public Builder contentType(String contentType) {
            this.video.contentType = contentType;
            return this;
        }

        public Builder size(Long size) {
            this.video.size = size;
            return this;
        }

        public Builder format(String format) {
            this.video.format = format;
            return this;
        }

        public Builder data(byte[] data) {
            this.video.data = data;
            return this;
        }

        public Video build() {
            return video;
        }
    }
}
