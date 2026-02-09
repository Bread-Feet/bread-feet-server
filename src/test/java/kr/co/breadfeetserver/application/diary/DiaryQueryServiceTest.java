package kr.co.breadfeetserver.application.diary;

import kr.co.breadfeetserver.domain.diary.*;
import kr.co.breadfeetserver.domain.diary.query.DiaryQueryRepository; // Added import
import kr.co.breadfeetserver.application.support.CursorService; // Added import
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static kr.co.breadfeetserver.fixture.DiaryFixture.aDiary;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiaryQuaryService 단위 테스트")
class DiaryQueryServiceTest {

    @Mock
    private DiaryQueryRepository diaryQueryRepository;
    @Mock
    private HashtagJpaRepository hashtagJpaRepository;
    @Mock
    private PictureUrlJpaRepository pictureUrlJpaRepository;
    @Mock
    private CursorService cursorService;

    private DiaryQueryService diaryQueryService;

    @BeforeEach
    void setUp() {
        diaryQueryService = new DiaryQueryService(diaryQueryRepository, hashtagJpaRepository, pictureUrlJpaRepository, cursorService); // Updated constructor
    }

    @Test
    @DisplayName("사용자는_빵집일지를_조회할_수_있다")
    void 사용자는_빵집일지를_조회할_수_있다() {
        // Given
        long diaryId = 1L;
        long memberId = 1L;
        Diary diary = aDiary(diaryId);
        Hashtag hashtag = Hashtag.builder().id(1L).name("소금빵").diaryId(diaryId).build();
        PictureUrl pictureUrl = PictureUrl.builder().id(1L).pic_url("picture_url").diaryId(diaryId).build();

        given(diaryQueryRepository.findById(anyLong())).willReturn(Optional.of(diary));
        given(hashtagJpaRepository.findAllByDiaryId(anyLong())).willReturn(Collections.singletonList(hashtag));
        given(pictureUrlJpaRepository.findAllByDiaryId(anyLong())).willReturn(Collections.singletonList(pictureUrl));

        // When
        DiaryResponse diaryResponse = diaryQueryService.getDiary(diaryId, memberId);

        // Then
        assertThat(diaryResponse.id()).isEqualTo(diaryId);
        assertThat(diaryResponse.hashtags().get(0)).isEqualTo("소금빵");
        assertThat(diaryResponse.pictureUrls().get(0)).isEqualTo("picture_url");
    }
}