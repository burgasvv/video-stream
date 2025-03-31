package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.FollowUpResponse;
import org.burgas.backendserver.dto.FollowedStreamer;
import org.burgas.backendserver.dto.Follower;
import org.burgas.backendserver.entity.FollowUp;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.repository.IdentityRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class FollowUpMapper {

    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;
    private final IdentityRepository identityRepository;

    public FollowUpMapper(
            StreamerRepository streamerRepository, StreamerMapper streamerMapper, IdentityRepository identityRepository
    ) {
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
        this.identityRepository = identityRepository;
    }

    public FollowUpResponse toFollowUpResponse(final FollowUp followUp) {
        Streamer streamer = this.streamerRepository.findById(followUp.getStreamerId()).orElseGet(Streamer::new);
        Identity streamerIdentity = this.identityRepository.findById(streamer.getIdentityId()).orElseGet(Identity::new);
        Identity follower = this.identityRepository.findById(followUp.getFollowerId()).orElseGet(Identity::new);
        return FollowUpResponse.builder()
                .streamer(
                        FollowedStreamer.builder()
                                .id(streamer.getId())
                                .nickname(streamerIdentity.getNickname())
                                .imageId(streamer.getImageId())
                                .categories(this.streamerMapper.toStreamerResponse(streamer).getCategories())
                                .build()
                )
                .follower(
                        Follower.builder()
                                .id(follower.getId())
                                .nickname(follower.getNickname())
                                .imageId(follower.getImageId())
                                .build()
                )
                .followedAt(followUp.getFollowedAt().format(ofPattern("dd.MM.hh, hh:mm")))
                .build();
    }
}
