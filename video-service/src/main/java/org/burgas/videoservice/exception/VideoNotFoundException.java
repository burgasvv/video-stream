package org.burgas.videoservice.exception;

public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(String message) {
        super(message);
    }
}
