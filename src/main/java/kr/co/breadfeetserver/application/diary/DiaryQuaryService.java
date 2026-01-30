package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.diary.HashtagJpaRepository;
import kr.co.breadfeetserver.domain.diary.PictureUrlJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQuaryService {
    private final DiaryJpaRepository diaryRepository;
    private final HashtagJpaRepository hashtagJpaRepository;
    private final PictureUrlJpaRepository pictureUrlJpaRepository;

    public DiaryResponse getDiary(Long id) {
        return getDiary(id, null);
    }

    public DiaryResponse getDiary(Long diaryid, Long currentMemberId) {
        Diary diary = diaryRepository.findById(diaryid)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.DIARY_NOT_FOUND));

        List<String> hashtags = hashtagJpaRepository.findAllByDiaryId(diaryid).stream()
                .map(hashtag -> hashtag.getName())
                .collect(Collectors.toList());

        List<String> pictureUrls = pictureUrlJpaRepository.findAllByDiaryId(diaryid).stream()
                .map(pictureUrl -> pictureUrl.getPic_url())
                .collect(Collectors.toList());

        return DiaryResponse.from(diary, hashtags, pictureUrls);
    }
}
