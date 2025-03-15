package org.burgas.videoservice.service;

import org.burgas.videoservice.dto.VideoRequest;
import org.burgas.videoservice.dto.VideoResponse;
import org.burgas.videoservice.exception.VideoNotFoundException;
import org.burgas.videoservice.mapper.VideoMapper;
import org.burgas.videoservice.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.burgas.videoservice.entity.VideoMessage.VIDEO_DELETED;
import static org.burgas.videoservice.entity.VideoMessage.VIDEO_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoService(VideoRepository videoRepository, VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
    }

    public List<VideoResponse> findAll() {
        return this.videoRepository
                .findAll()
                .stream()
                .map(videoMapper::toVideoResponse)
                .toList();
    }

    public List<VideoResponse> findByCategoryId(Long categoryId) {
        return this.videoRepository
                .findVideosByCategoryId(categoryId)
                .stream()
                .map(videoMapper::toVideoResponse)
                .toList();
    }

    public VideoResponse findById(final Long videoId) {
        return videoRepository
                .findById(videoId)
                .map(videoMapper::toVideoResponse)
                .orElseGet(VideoResponse::new);
    }

    public VideoResponse findByName(final String name) {
        return videoRepository
                .findVideoByName(name)
                .map(videoMapper::toVideoResponse)
                .orElseGet(VideoResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long uploadOrUpdate(final VideoRequest videoRequest, final MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            return videoMapper
                    .toVideoResponse(videoRepository.save(
                            videoMapper.toVideo(videoRequest, multipartFile)
                    ))
                    .getId();
        } else {
            return videoMapper
                    .toVideoResponse(videoRepository.save(
                            videoMapper.toVideo(videoRequest, null)
                    ))
                    .getId();
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(Long videoId) {
        return videoRepository
                .findById(videoId)
                .map(
                        video -> {
                            videoRepository.deleteById(video.getId());
                            return VIDEO_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new VideoNotFoundException(VIDEO_NOT_FOUND.getMessage()));
    }
}
