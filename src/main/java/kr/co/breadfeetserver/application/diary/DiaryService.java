package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.diary.Hashtag;
import kr.co.breadfeetserver.domain.diary.HashtagJpaRepository;
import kr.co.breadfeetserver.domain.diary.PictureUrl;
import kr.co.breadfeetserver.domain.diary.PictureUrlJpaRepository;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryJpaRepository diaryJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final HashtagJpaRepository hashtagJpaRepository;
    private final PictureUrlJpaRepository pictureUrlJpaRepository;

    public Long createDiary(Long memberId, DiaryCreateRequest request) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryJpaRepository.save(request.toEntity(memberId));
        Long diaryId = diary.getId();

        if (!CollectionUtils.isEmpty(request.hashtags())) {
            request.hashtags().forEach(hashtag ->
                    hashtagJpaRepository.save(
                            Hashtag.builder()
                                    .name(hashtag)
                                    .diaryId(diaryId)
                                    .build()
                    )
            );
        }

        if (!CollectionUtils.isEmpty(request.pictureUrls())) {
            request.pictureUrls().forEach(pictureUrl ->
                    pictureUrlJpaRepository.save(
                            PictureUrl.builder()
                                    .pic_url(pictureUrl)
                                    .diaryId(diaryId)
                                    .build()
                    )
            );
        }

        return diaryId;
    }

    public void updateDiary(long memberId, Long diaryId, DiaryUpdateRequest request) {
        Diary diary = diaryJpaRepository.findByIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryUpdateRequestToEntity(diary, request);

        hashtagJpaRepository.deleteAllByDiaryId(diaryId);
        if (!CollectionUtils.isEmpty(request.hashtags())) {
            request.hashtags().forEach(hashtag ->
                    hashtagJpaRepository.save(
                            Hashtag.builder()
                                    .name(hashtag)
                                    .diaryId(diaryId)
                                    .build()
                    )
            );
        }

        pictureUrlJpaRepository.deleteAllByDiaryId(diaryId);
        if (!CollectionUtils.isEmpty(request.pictureUrls())) {
            request.pictureUrls().forEach(pictureUrl ->
                    pictureUrlJpaRepository.save(
                            PictureUrl.builder()
                                    .pic_url(pictureUrl)
                                    .diaryId(diaryId)
                                    .build()
                    )
            );
        }
    }

    public void deleteDiary(long memberId, Long diaryId) {
        diaryJpaRepository.findByIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        hashtagJpaRepository.deleteAllByDiaryId(diaryId);
        pictureUrlJpaRepository.deleteAllByDiaryId(diaryId);
        diaryJpaRepository.deleteById(diaryId);
    }

    private void diaryUpdateRequestToEntity(Diary diary, DiaryUpdateRequest request) {
        diary.updateDiary(
                request.address().toEntity(),
                request.thumbnail(),
                request.isPublic(),
                request.visitDate(),
                request.title(),
                request.content(),
                request.bakeryId()
        );
    }
}