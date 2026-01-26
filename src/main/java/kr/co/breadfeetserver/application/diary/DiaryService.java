package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryJpaRepository diaryJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public Long createDiary(Long memberId, DiaryCreateRequest request) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));
        return diaryJpaRepository.save(request.toEntity(memberId)).getId();
    }

    public void updateDiary(long memberId, Long diaryId, DiaryUpdateRequest request) {
        Diary diary = diaryJpaRepository.findById(diaryId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryUpdateRequestToEntity(diary, request);
    }

    public void deleteDiary(long memberId, Long diaryId){
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