package org.burgas.backendserver.dto;

public record VideoRequest(
        Long id,
        Long categoryId,
        Long streamerId,
        String name,
        String description
) {
}
