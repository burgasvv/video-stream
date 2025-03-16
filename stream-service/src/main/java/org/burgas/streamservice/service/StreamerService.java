package org.burgas.streamservice.service;

import org.burgas.streamservice.dto.StreamerRequest;
import org.burgas.streamservice.dto.StreamerResponse;
import org.burgas.streamservice.mapper.StreamerMapper;
import org.burgas.streamservice.repository.StreamerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class StreamerService {

    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;

    public StreamerService(StreamerRepository streamerRepository, StreamerMapper streamerMapper) {
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
    }

    public List<StreamerResponse> findAll() {
        return this.streamerRepository
                .findAll()
                .stream()
                .map(streamerMapper::toStreamerResponse)
                .toList();
    }

    public StreamerResponse findById(Long streamId) {
        return this.streamerRepository
                .findById(streamId)
                .map(streamerMapper::toStreamerResponse)
                .orElseGet(StreamerResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(final StreamerRequest streamerRequest) {
        return this.streamerMapper
                .toStreamerSave(streamerRequest)
                .getId();
    }
}
