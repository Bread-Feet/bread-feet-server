//package kr.co.breadfeetserver.presentation.diary;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import kr.co.breadfeetserver.application.diary.DiaryQueryService;
//import kr.co.breadfeetserver.application.diary.DiaryService;
//import kr.co.breadfeetserver.domain.diary.Diary;
//import kr.co.breadfeetserver.fixture.DiaryFixture;
//import kr.co.breadfeetserver.infra.exception.GlobalExceptionHandler;
//import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
//import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
//import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//
//import static kr.co.breadfeetserver.fixture.DiaryFixture.aDiary;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("DiaryController 단위 테스트")
//class DiaryControllerTest {
//
//    @InjectMocks
//    private DiaryController diaryController;
//
//    @Mock
//    private DiaryService diaryService;
//    @Mock
//    private DiaryQueryService diaryQueryService;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    private DiaryCreateRequest diaryCreateRequest;
//    private DiaryUpdateRequest diaryUpdateRequest;
//    private final Long memberId = 1L;
//    private final Long diaryId = 1L;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(diaryController)
//                .setControllerAdvice(new GlobalExceptionHandler())
//                .build();
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//
//        diaryCreateRequest = DiaryFixture.aDiaryCreateRequest(Collections.emptyList(), Collections.emptyList());
//        diaryUpdateRequest = DiaryFixture.aDiaryUpdateRequest(diaryId);
//    }
//
//    @Test
//    @DisplayName("빵집일지_생성_성공")
//    void 빵집일지_생성_성공() throws Exception {
//        // Given
//        when(diaryService.createDiary(anyLong(), any(DiaryCreateRequest.class))).thenReturn(
//                diaryId);
//
//        // When & Then
//        mockMvc.perform(post("/diaries")
//                        .param("memberId", String.valueOf(memberId))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(diaryCreateRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data").value(diaryId))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("빵집일지_수정_성공")
//    void 빵집일지_수정_성공() throws Exception {
//        // Given
//        doNothing().when(diaryService)
//                .updateDiary(anyLong(), anyLong(), any(DiaryUpdateRequest.class));
//
//        // When & Then
//        mockMvc.perform(put("/diaries/{id}", diaryId)
//                        .param("memberId", String.valueOf(memberId))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(diaryUpdateRequest)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("빵집일지_삭제_성공")
//    void 빵집일지_삭제_성공() throws Exception {
//        // Given
//        doNothing().when(diaryService).deleteDiary(anyLong(), anyLong());
//
//        // When & Then
//        mockMvc.perform(delete("/diaries/{id}", diaryId)
//                        .param("memberId", String.valueOf(memberId)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("빵집일지_단건_조회_성공")
//    void 빵집일지_단건_조회_성공() throws Exception {
//        // Given
//        Diary diary = aDiary(diaryId);
//        DiaryResponse diaryResponse = DiaryResponse.from(diary, Collections.singletonList("소금빵"), Collections.singletonList("picture_url"));
//        when(diaryQueryService.getDiary(anyLong(), anyLong())).thenReturn(diaryResponse);
//
//        // When & Then
//        mockMvc.perform(get("/diaries/{id}", diaryId)
//                        .param("memberId", String.valueOf(memberId)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(diaryId))
//                .andExpect(jsonPath("$.data.hashtags[0]").value("소금빵"))
//                .andExpect(jsonPath("$.data.pictureUrls[0]").value("picture_url"))
//                .andDo(print());
//    }
//}
//
