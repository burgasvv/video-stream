package org.burgas.videoservice.dto;

public record VideoRequest(
        Long id,
        Long categoryId,
        Long streamerId,
        String name,
        String description
) {
}
