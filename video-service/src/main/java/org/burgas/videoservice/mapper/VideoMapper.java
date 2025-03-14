package org.burgas.videoservice.mapper;

import org.burgas.videoservice.dto.VideoRequest;
import org.burgas.videoservice.dto.VideoResponse;
import org.burgas.videoservice.entity.Category;
import org.burgas.videoservice.entity.Video;
import org.burgas.videoservice.exception.FileEmptyException;
import org.burgas.videoservice.repository.CategoryRepository;
import org.burgas.videoservice.repository.VideoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.burgas.videoservice.entity.VideoMessage.FILE_IS_EMPTY;

@Component
public class VideoMapper {

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
        Long videoId = getData(videoRequest.id(), 0L);
        return videoRepository
                .findById(videoId)
                .map(
                        video -> Video.builder()
                                .id(video.getId())
                                .name(getData(videoRequest.name(), video.getName()))
                                .categoryId(getData(videoRequest.categoryId(), video.getCategoryId()))
                                .description(getData(videoRequest.description(), video.getDescription()))
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
                                            .name(getData(videoRequest.name(), multipartFile.getOriginalFilename()))
                                            .categoryId(videoRequest.categoryId())
                                            .description(videoRequest.description())
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
                .description(video.getDescription())
                .size(video.getSize())
                .contentType(video.getContentType())
                .format(video.getFormat())
                .data(video.getData())
                .build();
    }
}
