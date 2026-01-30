package kr.co.breadfeetserver.presentation.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.Hashtag;
import kr.co.breadfeetserver.domain.diary.PictureUrl;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DiaryResponse(
        Long id,
        AddressJpaVO address,
        String thumbnailUrl,
        Integer score,
        Boolean isPublic,
        LocalDateTime visitDate,
        String content,
        Long memberId,
        Long bakeryId,
        List<String> hashtags,
        List<String> pictureUrls
) {
    public static DiaryResponse from(Diary diary, List<String> hashtags, List<String> pictureUrls){
        return DiaryResponse.builder()
                .id(diary.getId())
                .address(diary.getAddress())
                .thumbnailUrl(diary.getThumbnailUrl())
                .score(diary.getScore())
                .isPublic(diary.getIsPublic())
                .visitDate(diary.getVisitDate())
                .content(diary.getContent())
                .memberId(diary.getMemberId())
                .bakeryId(diary.getBakeryId())
                .hashtags(hashtags)
                .pictureUrls(pictureUrls)
                .build();
    }
}
/*package com.contech.tugoserver.presentation.post.dto;

import com.contech.tugoserver.domain.post.Post;
import com.contech.tugoserver.domain.post.PostType;
import com.contech.tugoserver.presentation.poll.dto.PollResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 응답 DTO")
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,

        @Schema(description = "작성자 정보")
        Author author,

        @Schema(description = "게시글 내용")
        String contentText,

        @Schema(description = "게시글 타입", example = "FREE")
        PostType postType,

        @Schema(description = "PPV 가격", example = "1000")
        Long ppvPrice,

        @Schema(description = "생성 시각")
        String createdAt,

        @Schema(description = "수정 시각")
        String updatedAt,

        @Schema(description = "통계 정보")
        Stats stats,

        @Schema(description = "미디어 URL 목록")
        List<String> mediaUrls,

        @Schema(description = "투표 정보")
        PollResponse poll,

        @Schema(description = "좋아요 여부")
        boolean isLiked,

        @Schema(description = "보관함 저장 여부")
        boolean isSaved
) {
    public static PostResponse from(Post post, Author author, Stats stats, List<String> mediaUrls, PollResponse poll, boolean isLiked, boolean isSaved) {
        return new PostResponse(
                post.getPostId(),
                author,
                post.getContentText(),
                post.getPostType(),
                post.getPpvPrice(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString(),
                stats,
                mediaUrls,
                poll,
                isLiked,
                isSaved
        );
    }

    public static PostResponse from(Post post) {
        Author defaultAuthor = new Author("Unknown", "unknown", null);
        Stats defaultStats = new Stats(0, 0);
        return new PostResponse(
                post.getPostId(),
                defaultAuthor,
                post.getContentText(),
                post.getPostType(),
                post.getPpvPrice(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString(),
                defaultStats,
                List.of(),
                null,
                false,
                false
        );
    }

    @Schema(description = "작성자 정보")
    public record Author(
            @Schema(description = "이름")
            String name,

            @Schema(description = "사용자명")
            String username,

            @Schema(description = "프로필 이미지 URL")
            String profileImageUrl
    ) {
    }

    @Schema(description = "통계 정보")
    public record Stats(
            @Schema(description = "댓글 수")
            int comments,

            @Schema(description = "좋아요 수")
            int likes
    ) {
    }
}

 */