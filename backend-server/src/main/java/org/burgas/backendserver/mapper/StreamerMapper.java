package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.dto.StreamerRequest;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.repository.IdentityRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Component;

@Component
public class StreamerMapper {

    private final StreamerRepository streamerRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public StreamerMapper(
            StreamerRepository streamerRepository,
            IdentityRepository identityRepository, IdentityMapper identityMapper
    ) {
        this.streamerRepository = streamerRepository;
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
                            this.identityRepository
                                    .updateIdentitySetIdentityAuthorityId(
                                            streamer.getIdentityId(), 3L
                                    );
                            return streamer;
                        }
                );
    }

    public StreamerResponse toStreamerResponse(final Streamer streamer) {
        return StreamerResponse.builder()
                .id(streamer.getId())
                .firstname(streamer.getFirstname())
                .lastname(streamer.getLastname())
                .patronymic(streamer.getPatronymic())
                .about(streamer.getAbout())
                .identity(
                        this.identityRepository
                                .findById(streamer.getIdentityId())
                                .map(identityMapper::toIdentityResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .imageId(streamer.getImageId())
                .build();
    }
}
