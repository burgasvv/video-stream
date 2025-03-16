package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.dto.StreamerRequest;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.entity.IdentityStreamerToken;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.repository.IdentityRepository;
import org.burgas.backendserver.repository.IdentityStreamerTokenRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StreamerMapper {

    private final StreamerRepository streamerRepository;
    private final IdentityStreamerTokenRepository identityStreamerTokenRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public StreamerMapper(
            StreamerRepository streamerRepository,
            IdentityStreamerTokenRepository identityStreamerTokenRepository, IdentityRepository identityRepository, IdentityMapper identityMapper
    ) {
        this.streamerRepository = streamerRepository;
        this.identityStreamerTokenRepository = identityStreamerTokenRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    public Streamer toStreamerSave(final StreamerRequest streamerRequest) {
        Long streamerId = getData(streamerRequest.getId(), 0L);
        return this.streamerRepository
                .findById(streamerId)
                .map(
                        streamer -> this.streamerRepository.save(
                                Streamer.builder()
                                        .id(streamer.getId())
                                        .identityId(this.getData(streamerRequest.getIdentityId(), streamer.getIdentityId()))
                                        .firstname(this.getData(streamerRequest.getFirstname(), streamer.getFirstname()))
                                        .lastname(this.getData(streamerRequest.getLastname(), streamer.getLastname()))
                                        .patronymic(this.getData(streamerRequest.getPatronymic(), streamer.getPatronymic()))
                                        .about(this.getData(streamerRequest.getAbout(), streamer.getAbout()))
                                        .build()
                        )
                )
                .orElseGet(
                        () -> {
                            Streamer streamer = this.streamerRepository.save(
                                    Streamer.builder()
                                            .identityId(streamerRequest.getIdentityId())
                                            .firstname(streamerRequest.getFirstname())
                                            .lastname(streamerRequest.getLastname())
                                            .patronymic(streamerRequest.getPatronymic())
                                            .about(streamerRequest.getAbout())
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
        IdentityResponse identityResponse = this.identityRepository
                .findIdentityByIdentityStreamerToken(identityStreamerToken != null ? identityStreamerToken.getToken() : null)
                .map(identityMapper::toIdentityResponse)
                .orElseGet(IdentityResponse::new);
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
