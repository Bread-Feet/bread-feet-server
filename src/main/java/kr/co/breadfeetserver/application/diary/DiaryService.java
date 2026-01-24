package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest; // DTO import
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryJpaRepository diaryJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public Long createDiary(Long memberId, DiaryCreateRequest request) {
        // The toEntity method in the request DTO is used, but it doesn't actually use the memberId.
        // This is because the Diary entity does not have a proper relationship with Member.
        // We will proceed as per user's instruction to not modify Diary entity at this stage.
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));
        return diaryJpaRepository.save(request.toEntity(memberId)).getId();
    }

    public void updateDiary(long memberId, Long diaryId, DiaryUpdateRequest request) {
        Diary diary = diaryJpaRepository.findById(diaryId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryUpdateRequestToEntity(diary, request);
    }

    public void deleteBakery(long memberId, Long diaryId){
        Diary diary = diaryJpaRepository.findById(diaryId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryJpaRepository.delete(diary);
    }
    private void diaryUpdateRequestToEntity(Diary diary, DiaryUpdateRequest request) {
        diary.updateDiary(
                request.address().toEntity(),
                request.thumbnail(),
                request.score(),
                request.isPublic(),
                request.visitDate(),
                request.content()
        );
    }
}

/*
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostJpaRepository postRepository;
    private final ProfileJpaRepository profileRepository;
    private final MediaJpaRepository mediaRepository;
    private final PollJpaRepository pollRepository;
    private final PollService pollService;
    private final MentionService mentionService;

    public Long createPost(Long memberId, PostCreateRequest request) {
        profileRepository.findById(memberId)
                .orElseThrow(() -> new TugoRuntimeException(ErrorCode.USER_NOT_FOUND));

        // Post 생성 (모든 Member가 게시물 작성 가능)
        Post post = postRepository.save(request.toEntity(memberId));

        // 미디어 URL이 있으면 Media 엔티티 생성
        if (request.mediaUrls() != null && !request.mediaUrls().isEmpty()) {
            List<Media> mediaList = request.mediaUrls().stream()
                    .map(url -> Media.builder()
                            .fileUrl(url)
                            .mediaType(MediaType.IMAGE)
                            .postId(post.getPostId())
                            .uploaderId(memberId)
                            .ppvPrice(null)
                            .build())
                    .toList();
            mediaRepository.saveAll(mediaList);
        }

        // Poll 데이터가 있으면 Poll 생성
        if (request.pollData() != null) {
            PollCreateData pollData = request.pollData();
            Poll poll = Poll.builder()
                    .question(pollData.question())
                    .pollType(pollData.pollType())
                    .endDate(pollData.endDate())
                    .postId(post.getPostId())
                    .memberId(memberId)
                    .build();
            pollService.createPollWithOptions(poll, pollData.options());
        }

        // 멘션 파싱 및 저장
        mentionService.processMentions(request.contentText(), post.getPostId(), null, memberId);

        return post.getPostId();
    }

    public void updatePost(Long memberId, Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TugoRuntimeException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new TugoRuntimeException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        post.update(request);

        // 미디어 URL 업데이트 (기존 미디어 삭제 후 재생성)
        if (request.mediaUrls() != null) {
            // 기존 미디어 삭제
            List<Media> existingMedia = mediaRepository.findAllByPostId(id, Pageable.unpaged()).getContent();
            mediaRepository.deleteAll(existingMedia);

            // 새 미디어 생성
            if (!request.mediaUrls().isEmpty()) {
                List<Media> mediaList = request.mediaUrls().stream()
                        .map(url -> Media.builder()
                                .fileUrl(url)
                                .mediaType(MediaType.IMAGE)
                                .postId(id)
                                .uploaderId(memberId)
                                .ppvPrice(null)
                                .build())
                        .toList();
                mediaRepository.saveAll(mediaList);
            }
        }
    }


    public void deletePost(Long memberId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TugoRuntimeException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new TugoRuntimeException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        // 연관된 미디어 먼저 삭제
        List<Media> mediaList = mediaRepository.findAllByPostId(id, Pageable.unpaged()).getContent();
        mediaRepository.deleteAll(mediaList);

        // 연관된 Poll 삭제 (있는 경우)
        pollRepository.findByPostId(id).ifPresent(poll -> {
            pollService.deletePollAndVotes(poll.getPollId());
        });

        postRepository.delete(post);
    }
}
*/