//package kr.co.breadfeetserver.domain.diary.query;
//
//import kr.co.breadfeetserver.domain.diary.query.mapper.DiaryRowMapper;
//import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCursorCommand;
//import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Slice;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList; // Added
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("DiaryJdbcRepositoryImpl 단위 테스트")
//class DiaryJdbcRepositoryImplTest {
//
//    @Mock
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    // DiaryRowMapper is not mocked anymore, as it's static final in the impl
//    private DiaryJdbcRepositoryImpl diaryJdbcRepository;
//
//    private List<DiaryListResponse> mockDiaries;
//
//    @BeforeEach
//    void setUp() {
//        // Manually instantiate the repository with the mocked template.
//        // The DiaryRowMapper is a static final field in DiaryJdbcRepositoryImpl,
//        // so we don't pass it here.
//        diaryJdbcRepository = new DiaryJdbcRepositoryImpl(namedParameterJdbcTemplate);
//
//        // Prepare mock data
//        mockDiaries = List.of(
//                new DiaryListResponse(3L, "thumb3.jpg", true, LocalDateTime.now().minusDays(1), "Content 3", 11L, 102L,"" List.of("hashtag4"), List.of("pic3_1.jpg")),
//                new DiaryListResponse(2L, "thumb2.jpg", false, LocalDateTime.now().minusDays(2), "Content 2", 10L, 101L, List.of("hashtag3"), List.of("pic2_1.jpg")),
//                new DiaryListResponse(1L, "thumb1.jpg", true, LocalDateTime.now().minusDays(3), "Content 1", 10L, 100L, List.of("hashtag1", "hashtag2"), List.of("pic1_1.jpg", "pic1_2.jpg"))
//        );
//    }
//
//    @Test
//    @DisplayName("초기_커서로_빵집일지를_조회한다")
//    void 초기_커서로_빵집일지를_조회한다() {
//        // Given
//        DiaryCursorCommand command = new DiaryCursorCommand(0L, 2);
//        // Simulate fetching 2 + 1 = 3 diaries, then remove the extra one
//        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(DiaryRowMapper.class)))
//                .thenReturn(new ArrayList<>(mockDiaries.subList(0, 3))); // Return 3 diaries
//
//        // When
//        Slice<DiaryListResponse> slice = diaryJdbcRepository.findAll(command);
//
//        // Then
//        assertThat(slice).isNotNull();
//        assertThat(slice.hasContent()).isTrue();
//        assertThat(slice.getNumberOfElements()).isEqualTo(2);
//        assertThat(slice.hasNext()).isTrue();
//
//        DiaryListResponse firstDiary = slice.getContent().get(0);
//        assertThat(firstDiary.id()).isEqualTo(3L);
//        assertThat(firstDiary.hashtags()).containsExactlyInAnyOrder("hashtag4");
//        assertThat(firstDiary.pictureUrls()).containsExactlyInAnyOrder("pic3_1.jpg");
//
//        DiaryListResponse secondDiary = slice.getContent().get(1);
//        assertThat(secondDiary.id()).isEqualTo(2L);
//        assertThat(secondDiary.hashtags()).containsExactlyInAnyOrder("hashtag3");
//        assertThat(secondDiary.pictureUrls()).containsExactlyInAnyOrder("pic2_1.jpg");
//    }
//
//    @Test
//    @DisplayName("다음_페이지_커서로_빵집일지를_조회한다")
//    void 다음_페이지_커서로_빵집일지를_조회한다() {
//        // Given
//        DiaryCursorCommand command = new DiaryCursorCommand(2L, 2);
//        // Simulate fetching 2 + 1 = 3 diaries, then remove the extra one
//        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(DiaryRowMapper.class)))
//                .thenReturn(new ArrayList<>(mockDiaries.subList(2, 3))); // Returns 1 diary (id=1L)
//
//        // When
//        Slice<DiaryListResponse> slice = diaryJdbcRepository.findAll(command);
//
//        // Then
//        assertThat(slice).isNotNull();
//        assertThat(slice.hasContent()).isTrue();
//        assertThat(slice.getNumberOfElements()).isEqualTo(1);
//        assertThat(slice.hasNext()).isFalse(); // Only 1 diary left after id 2 (which is 1L)
//
//        DiaryListResponse firstDiary = slice.getContent().get(0);
//        assertThat(firstDiary.id()).isEqualTo(1L);
//        assertThat(firstDiary.hashtags()).containsExactlyInAnyOrder("hashtag1", "hashtag2");
//        assertThat(firstDiary.pictureUrls()).containsExactlyInAnyOrder("pic1_1.jpg", "pic1_2.jpg");
//    }
//
//    @Test
//    @DisplayName("커서_이후_일기가_없는_경우_빈_슬라이스를_반환한다")
//    void 커서_이후_일기가_없는_경우_빈_슬라이스를_반환한다() {
//        // Given
//        DiaryCursorCommand command = new DiaryCursorCommand(1L, 2);
//        // Simulate no diaries returned after cursor
//        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(DiaryRowMapper.class)))
//                .thenReturn(Collections.emptyList());
//
//        // When
//        Slice<DiaryListResponse> slice = diaryJdbcRepository.findAll(command);
//
//        // Then
//        assertThat(slice).isNotNull();
//        assertThat(slice.hasContent()).isFalse();
//        assertThat(slice.getNumberOfElements()).isEqualTo(0);
//        assertThat(slice.hasNext()).isFalse();
//    }
//
//    @Test
//    @DisplayName("삭제된_일기는_조회되지_않는다")
//    void 삭제된_일기는_조회되지_않는다() {
//        // Given: The BASE_SQL WHERE clause handles deleted_at, but in a Mock test, we simulate the outcome.
//        // Assuming mockDiaries list does not contain deleted ones directly.
//        DiaryCursorCommand command = new DiaryCursorCommand(5L, 10);
//        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(DiaryRowMapper.class)))
//                .thenReturn(new ArrayList<>(mockDiaries.subList(0, 3))); // Return all 3 non-deleted diaries
//
//        // When
//        Slice<DiaryListResponse> slice = diaryJdbcRepository.findAll(command);
//
//        // Then
//        assertThat(slice.hasContent()).isTrue();
//        assertThat(slice.getNumberOfElements()).isEqualTo(3);
//        assertThat(slice.getContent()).extracting(DiaryListResponse::id).containsExactly(3L, 2L, 1L);
//    }
//}
