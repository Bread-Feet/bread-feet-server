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
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        Optional.ofNullable(request.hashtags())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(name -> Hashtag.builder().name(name).diaryId(diary.getId()).build())
                .forEach(hashtagJpaRepository::save);

        Optional.ofNullable(request.pictureUrls())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(url -> PictureUrl.builder().pic_url(url).diaryId(diary.getId()).build())
                .forEach(pictureUrlJpaRepository::save);

        return diary.getId();
    }

    public void updateDiary(long memberId, Long diaryId, DiaryUpdateRequest request) {
        Diary diary = diaryJpaRepository.findByIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryUpdateRequestToEntity(diary, request);
    }

    public void deleteDiary(long memberId, Long diaryId){
        Diary diary = diaryJpaRepository.findByIdAndMemberId(diaryId, memberId)
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