package org.burgas.videoservice.dto;

public record VideoRequest(
        Long id,
        Long categoryId,
        String name,
        String description
) {
}
