package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.StreamRequest;
import org.burgas.backendserver.dto.StreamResponse;
import org.burgas.backendserver.exception.StreamHasAlreadyStartedException;
import org.burgas.backendserver.mapper.StreamMapper;
import org.burgas.backendserver.repository.StreamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.burgas.backendserver.message.StreamMessage.STREAM_HAS_ALREADY_STARTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class StreamService {

    private final StreamRepository streamRepository;
    private final StreamMapper streamMapper;

    public StreamService(StreamRepository streamRepository, StreamMapper streamMapper) {
        this.streamRepository = streamRepository;
        this.streamMapper = streamMapper;
    }

    public List<StreamResponse> findByStreamerId(final Long streamerId) {
        return this.streamRepository
                .findStreamsByStreamerId(streamerId)
                .stream()
                .map(this.streamMapper::toStreamResponse)
                .toList();
    }

    public StreamResponse findById(final Long streamId) {
        return this.streamRepository
                .findById(streamId)
                .map(this.streamMapper::toStreamResponse)
                .orElseGet(StreamResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long startUpdateOrStop(final StreamRequest streamRequest) {
        if (
                this.streamRepository.existsStreamByStreamerIdAndIsLive(streamRequest.getStreamerId(), true) &&
                streamRequest.getId() != null
        ) {
            return handleStream(streamRequest);

        } else if (
                !this.streamRepository.existsStreamByStreamerIdAndIsLive(streamRequest.getStreamerId(), true) &&
                streamRequest.getId() == null
        ){
            return handleStream(streamRequest);

        } else {
            throw new StreamHasAlreadyStartedException(STREAM_HAS_ALREADY_STARTED.getMessage());
        }
    }

    private Long handleStream(StreamRequest streamRequest) {
        return this.streamRepository
                .save(this.streamMapper.toStream(streamRequest))
                .getId();
    }
}
