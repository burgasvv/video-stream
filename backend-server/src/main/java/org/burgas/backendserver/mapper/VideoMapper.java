package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.VideoRequest;
import org.burgas.backendserver.dto.VideoResponse;
import org.burgas.backendserver.entity.Category;
import org.burgas.backendserver.entity.Video;
import org.burgas.backendserver.exception.FileEmptyException;
import org.burgas.backendserver.repository.CategoryRepository;
import org.burgas.backendserver.repository.VideoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.burgas.backendserver.message.VideoMessage.FILE_IS_EMPTY;

@Component
public final class VideoMapper {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;

    public VideoMapper(VideoRepository videoRepository, CategoryRepository categoryRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
    }

    private <T> T getData(T first, T second) {
        return first == null ? second : first;
    }

    public Video toVideo(final VideoRequest videoRequest, final MultipartFile multipartFile) {
        Long videoId = getData(videoRequest.getId(), 0L);
        return videoRepository
                .findById(videoId)
                .map(
                        video -> Video.builder()
                                .id(video.getId())
                                .name(getData(videoRequest.getName(), video.getName()))
                                .categoryId(getData(videoRequest.getCategoryId(), video.getCategoryId()))
                                .streamerId(getData(videoRequest.getStreamerId(), video.getStreamerId()))
                                .streamId(getData(videoRequest.getStreamId(), video.getStreamId()))
                                .description(getData(videoRequest.getDescription(), video.getDescription()))
                                .contentType(video.getContentType())
                                .format(video.getFormat())
                                .size(video.getSize())
                                .data(video.getData())
                                .build()
                )
                .orElseGet(
                        () -> {
                            try {
                                if (multipartFile != null && !multipartFile.isEmpty()) {
                                    return Video.builder()
                                            .name(getData(videoRequest.getName(), multipartFile.getOriginalFilename()))
                                            .categoryId(videoRequest.getCategoryId())
                                            .streamerId(videoRequest.getStreamerId())
                                            .streamId(videoRequest.getStreamId())
                                            .description(videoRequest.getDescription())
                                            .contentType(multipartFile.getContentType())
                                            .size(multipartFile.getSize())
                                            .format(requireNonNull(multipartFile.getContentType()).split("/")[1])
                                            .data(multipartFile.getBytes())
                                            .build();
                                } else {
                                    throw new FileEmptyException(FILE_IS_EMPTY.getMessage());
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    public VideoResponse toVideoResponse(final Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .name(video.getName())
                .category(
                        categoryRepository
                                .findById(video.getCategoryId())
                                .orElseGet(Category::new)
                                .getName()
                )
                .streamerId(video.getStreamerId())
                .streamId(video.getStreamId())
                .description(video.getDescription())
                .size(video.getSize())
                .contentType(video.getContentType())
                .format(video.getFormat())
                .data(video.getData())
                .build();
    }
}
