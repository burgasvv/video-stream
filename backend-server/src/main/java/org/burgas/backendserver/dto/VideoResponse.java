package org.burgas.backendserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class VideoResponse {

    private Long id;
    private String name;
    private String category;
    private Long streamerId;
    private Long streamId;
    private String description;
    private String contentType;
    private Long size;
    private String format;

    @JsonIgnore
    private byte[] data;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getCategory() {
        return category;
    }

    @SuppressWarnings("unused")
    public void setCategory(String category) {
        this.category = category;
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
    public Long getStreamId() {
        return streamId;
    }

    @SuppressWarnings("unused")
    public void setStreamId(Long streamId) {
        this.streamId = streamId;
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

        private final VideoResponse videoResponse;

        public Builder() {
            videoResponse = new VideoResponse();
        }

        public Builder id(Long id) {
            this.videoResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.videoResponse.name = name;
            return this;
        }

        public Builder category(String category) {
            this.videoResponse.category = category;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.videoResponse.streamerId = streamerId;
            return this;
        }

        public Builder streamId(Long streamId) {
            this.videoResponse.streamId = streamId;
            return this;
        }

        public Builder description(String description) {
            this.videoResponse.description = description;
            return this;
        }

        public Builder contentType(String contentType) {
            this.videoResponse.contentType = contentType;
            return this;
        }

        public Builder size(Long size) {
            this.videoResponse.size = size;
            return this;
        }

        public Builder format(String format) {
            this.videoResponse.format = format;
            return this;
        }

        public Builder data(byte[] data) {
            this.videoResponse.data = data;
            return this;
        }

        public VideoResponse build() {
            return videoResponse;
        }
    }
}
