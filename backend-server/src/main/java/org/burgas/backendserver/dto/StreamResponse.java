package org.burgas.backendserver.dto;

@SuppressWarnings(value = "unused")
public final class StreamResponse {

    private Long id;
    private String name;
    private String streamer;
    private String category;
    private String started;
    private String ended;
    private Boolean isLive;
    private Boolean isSecured;

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

    public String getStreamer() {
        return streamer;
    }

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public Boolean getSecured() {
        return isSecured;
    }

    public void setSecured(Boolean secured) {
        isSecured = secured;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final StreamResponse streamResponse;

        public Builder() {
            streamResponse = new StreamResponse();
        }

        public Builder id(Long id) {
            this.streamResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.streamResponse.name = name;
            return this;
        }

        public Builder streamer(String streamer) {
            this.streamResponse.streamer = streamer;
            return this;
        }

        public Builder category(String category) {
            this.streamResponse.category = category;
            return this;
        }

        public Builder started(String started) {
            this.streamResponse.started = started;
            return this;
        }

        public Builder ended(String ended) {
            this.streamResponse.ended = ended;
            return this;
        }

        public Builder isLive(Boolean isLive) {
            this.streamResponse.isLive = isLive;
            return this;
        }

        public Builder isSecured(Boolean isSecured) {
            this.streamResponse.isSecured = isSecured;
            return this;
        }

        public StreamResponse build() {
            return streamResponse;
        }
    }
}
