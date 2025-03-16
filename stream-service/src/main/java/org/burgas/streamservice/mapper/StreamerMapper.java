package org.burgas.streamservice.mapper;

import org.burgas.streamservice.dto.IdentityResponse;
import org.burgas.streamservice.dto.StreamerRequest;
import org.burgas.streamservice.dto.StreamerResponse;
import org.burgas.streamservice.entity.IdentityStreamerToken;
import org.burgas.streamservice.entity.Streamer;
import org.burgas.streamservice.handler.RestClientHandler;
import org.burgas.streamservice.repository.IdentityStreamerTokenRepository;
import org.burgas.streamservice.repository.StreamerRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StreamerMapper {

    private final StreamerRepository streamerRepository;
    private final IdentityStreamerTokenRepository identityStreamerTokenRepository;
    private final RestClientHandler restClientHandler;

    public StreamerMapper(
            StreamerRepository streamerRepository,
            IdentityStreamerTokenRepository identityStreamerTokenRepository, RestClientHandler restClientHandler
    ) {
        this.streamerRepository = streamerRepository;
        this.identityStreamerTokenRepository = identityStreamerTokenRepository;
        this.restClientHandler = restClientHandler;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    public Streamer toStreamerSave(final StreamerRequest streamerRequest) {
        Long streamerId = getData(streamerRequest.id(), 0L);
        return this.streamerRepository
                .findById(streamerId)
                .map(
                        streamer -> this.streamerRepository.save(
                                Streamer.builder()
                                        .id(streamer.getId())
                                        .identityId(this.getData(streamerRequest.identityId(), streamer.getIdentityId()))
                                        .firstname(this.getData(streamerRequest.firstname(), streamer.getFirstname()))
                                        .lastname(this.getData(streamerRequest.lastname(), streamer.getLastname()))
                                        .patronymic(this.getData(streamerRequest.patronymic(), streamer.getPatronymic()))
                                        .about(this.getData(streamerRequest.about(), streamer.getAbout()))
                                        .build()
                        )
                )
                .orElseGet(
                        () -> {
                            Streamer streamer = this.streamerRepository.save(
                                    Streamer.builder()
                                            .identityId(streamerRequest.identityId())
                                            .firstname(streamerRequest.firstname())
                                            .lastname(streamerRequest.lastname())
                                            .patronymic(streamerRequest.patronymic())
                                            .about(streamerRequest.about())
                                            .build()
                            );
                            this.identityStreamerTokenRepository.save(
                                    IdentityStreamerToken.builder()
                                            .identityId(streamer.getIdentityId())
                                            .streamerId(streamer.getId())
                                            .token(UUID.randomUUID())
                                            .build()
                            );
                            this.streamerRepository
                                    .updateIdentitySetIdentityAuthorityId(
                                            streamer.getIdentityId(), 3L
                                    );
                            return streamer;
                        }
                );
    }

    public StreamerResponse toStreamerResponse(final Streamer streamer) {
        IdentityStreamerToken identityStreamerToken = this.identityStreamerTokenRepository
                .findIdentityStreamerTokenByIdentityIdAndStreamerId(streamer.getIdentityId(), streamer.getId())
                .orElse(null);
        IdentityResponse identityResponse = this.restClientHandler
                .getIdentityResponseByIdentityStreamerToken(
                        identityStreamerToken != null ? identityStreamerToken.getToken() : null
                ).getBody();
        return StreamerResponse.builder()
                .id(streamer.getId())
                .firstname(streamer.getFirstname())
                .lastname(streamer.getLastname())
                .patronymic(streamer.getPatronymic())
                .about(streamer.getAbout())
                .identity(identityResponse)
                .build();
    }
}
