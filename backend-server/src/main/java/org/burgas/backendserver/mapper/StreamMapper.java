package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.StreamRequest;
import org.burgas.backendserver.dto.StreamResponse;
import org.burgas.backendserver.entity.Category;
import org.burgas.backendserver.entity.Stream;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.repository.CategoryRepository;
import org.burgas.backendserver.repository.StreamRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public class StreamMapper {

    private final StreamRepository streamRepository;
    private final CategoryRepository categoryRepository;
    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;

    public StreamMapper(
            StreamRepository streamRepository,
            CategoryRepository categoryRepository,
            StreamerRepository streamerRepository, StreamerMapper streamerMapper
    ) {
        this.streamRepository = streamRepository;
        this.categoryRepository = categoryRepository;
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    @SuppressWarnings("unused")
    public Stream toStream(final StreamRequest streamRequest) {
        Long streamId = getData(streamRequest.getId(), 0L);
        return this.streamRepository
                .findById(streamId)
                .map(
                        stream -> Stream.builder()
                                .id(stream.getId())
                                .name(getData(streamRequest.getName(), stream.getName()))
                                .categoryId(getData(streamRequest.getCategoryId(), stream.getCategoryId()))
                                .streamerId(getData(streamRequest.getStreamerId(), stream.getStreamerId()))
                                .isLive(getData(streamRequest.getLive(), stream.getLive()))
                                .started(stream.getStarted())
                                .ended(!streamRequest.getLive() ? LocalDateTime.now() : stream.getEnded())
                                .build()
                )
                .orElseGet(
                        () -> Stream.builder()
                                .name(streamRequest.getName())
                                .streamerId(streamRequest.getStreamerId())
                                .categoryId(streamRequest.getCategoryId())
                                .started(LocalDateTime.now())
                                .isLive(true)
                                .build()
                );
    }

    public StreamResponse toStreamResponse(final Stream stream) {
        return StreamResponse.builder()
                .id(stream.getId())
                .name(stream.getName())
                .started(stream.getStarted().format(ofPattern("dd MMMM yyyy, hh:mm")))
                .ended(stream.getEnded().format(ofPattern("dd MMMM yyyy, hh:mm")))
                .category(
                        this.categoryRepository
                                .findById(stream.getCategoryId())
                                .orElseGet(Category::new)
                                .getName()
                )
                .streamer(
                        this.streamerMapper
                                .toStreamerResponse(
                                        this.streamerRepository
                                                .findById(stream.getStreamerId())
                                                .orElseGet(Streamer::new)
                                )
                                .getIdentity()
                                .getNickname()
                )
                .isLive(stream.getLive())
                .build();
    }
}
