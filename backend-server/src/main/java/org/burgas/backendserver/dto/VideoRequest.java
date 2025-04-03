package org.burgas.backendserver.dto;

@SuppressWarnings("unused")
public final class VideoRequest {

    private Long id;
    private Long categoryId;
    private Long streamerId;
    private Long streamId;
    private String name;
    private String description;

    public VideoRequest(Long id, Long categoryId, Long streamerId, Long streamId, String name, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.streamerId = streamerId;
        this.streamId = streamId;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
