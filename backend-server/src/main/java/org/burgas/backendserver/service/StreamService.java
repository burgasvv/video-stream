package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.StreamResponse;
import org.burgas.backendserver.mapper.StreamMapper;
import org.burgas.backendserver.repository.StreamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
