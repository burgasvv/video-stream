package org.burgas.streamservice.dto;

@SuppressWarnings("unused")
public record StreamerRequest(
        Long id,
        Long identityId,
        String firstname,
        String lastname,
        String patronymic,
        String about
) {
}
