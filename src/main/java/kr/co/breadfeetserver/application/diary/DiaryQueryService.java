package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.application.support.CursorService;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.diary.HashtagJpaRepository;
import kr.co.breadfeetserver.domain.diary.PictureUrlJpaRepository;
import kr.co.breadfeetserver.domain.diary.query.DiaryQueryRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCursorCommand;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryService {
    private final DiaryQueryRepository diaryJdbcRepository;
    private final kr.co.breadfeetserver.domain.member.MemberJpaRepository memberJpaRepository;
    private final HashtagJpaRepository hashtagJpaRepository;
    private final PictureUrlJpaRepository pictureUrlJpaRepository;
    private final CursorService cursorService;

    public CursorResponse<DiaryListResponse> getDiaryList(DiaryCursorCommand command) {
        final Slice<DiaryListResponse> slice = diaryJdbcRepository.findAll(command);
        return cursorService.getCursorResponse(slice);
    }

    public DiaryResponse getDiary(Long id) {
        return getDiary(id, null);
    }

    public DiaryResponse getDiary(Long diaryid, Long currentMemberId) {
        Diary diary = diaryJdbcRepository.findById(diaryid)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        String nickname = memberJpaRepository.findById(diary.getMemberId())
                .map(member -> member.getNickname())
                .orElse(null);

        List<String> hashtags = hashtagJpaRepository.findAllByDiaryId(diaryid).stream()
                .map(hashtag -> hashtag.getName())
                .collect(Collectors.toList());

        List<String> pictureUrls = pictureUrlJpaRepository.findAllByDiaryId(diaryid).stream()
                .map(pictureUrl -> pictureUrl.getPic_url())
                .collect(Collectors.toList());

        return DiaryResponse.from(diary, nickname, hashtags, pictureUrls);
    }
}
