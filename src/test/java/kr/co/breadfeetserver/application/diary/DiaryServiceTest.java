package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.diary.Hashtag;
import kr.co.breadfeetserver.domain.diary.HashtagJpaRepository;
import kr.co.breadfeetserver.domain.diary.PictureUrl;
import kr.co.breadfeetserver.domain.diary.PictureUrlJpaRepository;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kr.co.breadfeetserver.fixture.DiaryFixture.aDiary;
import static kr.co.breadfeetserver.fixture.DiaryFixture.aDiaryCreateRequest;
import static kr.co.breadfeetserver.fixture.DiaryFixture.aDiaryUpdateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiaryService 단위 테스트")
class DiaryServiceTest {

    @Mock
    private DiaryJpaRepository repository;
    @Mock
    private MemberJpaRepository memberJpaRepository;
    @Mock
    private HashtagJpaRepository hashtagJpaRepository;
    @Mock
    private PictureUrlJpaRepository pictureUrlJpaRepository;

    private DiaryService diaryService;

    @BeforeEach
    void setUp() {
        diaryService = new DiaryService(repository, memberJpaRepository, hashtagJpaRepository, pictureUrlJpaRepository);
    }

    @Test
    @DisplayName("사용자는_빵집일지를_생성할_수_있다")
    void 사용자는_빵집일지를_생성할_수_있다() {
        // Given
        long memberId = 1L;
        List<String> hashtags = List.of("빵맛집", "데이트");
        List<String> pictureUrls = List.of("url1", "url2");
        DiaryCreateRequest request = aDiaryCreateRequest(hashtags, pictureUrls);
        Diary diary = aDiary(1L);

        given(memberJpaRepository.findById(any(Long.class))).willReturn(Optional.of(Member.builder().build()));
        given(repository.save(any(Diary.class))).willReturn(diary);
        given(hashtagJpaRepository.save(any(Hashtag.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(pictureUrlJpaRepository.save(any(PictureUrl.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Long diaryId = diaryService.createDiary(memberId, request);

        // Then
        assertThat(diaryId).isEqualTo(1L);
        verify(repository).save(any(Diary.class));
        verify(hashtagJpaRepository, times(hashtags.size())).save(any(Hashtag.class));
        verify(pictureUrlJpaRepository, times(pictureUrls.size())).save(any(PictureUrl.class));
    }

    @Test
    @DisplayName("사용자는_빵집일지를_수정할_수_있다")
    void 사용자는_빵집일지를_수정할_수_있다() {
        // Given
        long memberId = 1L;
        long diaryId = 1L;
        DiaryUpdateRequest request = aDiaryUpdateRequest(diaryId);
        Diary diary = aDiary(diaryId);
        given(repository.findByIdAndMemberId(diaryId, memberId)).willReturn(Optional.of(diary));

        // When
        diaryService.updateDiary(memberId, diaryId, request);

        // Then
        // assertThat(diary.getScore()).isEqualTo(request.score());
        verify(repository).findByIdAndMemberId(diaryId, memberId);
    }

    @Test
    @DisplayName("사용자는_빵집일지를_삭제할_수_있다")
    void 사용자는_빵집일지를_삭제할_수_있다() {
        // Given
        long memberId = 1L;
        long diaryId = 1L;
        Diary diary = Diary.builder().memberId(memberId).build();
        given(repository.findByIdAndMemberId(diaryId, memberId)).willReturn(Optional.of(diary));
        doNothing().when(hashtagJpaRepository).deleteAllByDiaryId(diaryId);
        doNothing().when(pictureUrlJpaRepository).deleteAllByDiaryId(diaryId);
        doNothing().when(repository).deleteById(diaryId);

        // When
        diaryService.deleteDiary(memberId, diaryId);

        // Then
        verify(hashtagJpaRepository).deleteAllByDiaryId(diaryId);
        verify(pictureUrlJpaRepository).deleteAllByDiaryId(diaryId);
        verify(repository).deleteById(diaryId);
    }
}
