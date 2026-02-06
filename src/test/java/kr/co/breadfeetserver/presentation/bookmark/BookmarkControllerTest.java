package kr.co.breadfeetserver.presentation.bookmark;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.breadfeetserver.application.bookmark.BookmarkService;
import kr.co.breadfeetserver.infra.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkController 단위 테스트")
class BookmarkControllerTest {

    @InjectMocks
    private BookmarkController bookmarkController;

    @Mock
    private BookmarkService bookmarkService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private final Long memberId = 1L;
    private final Long bakeryId = 1L;
    private final Long bookmarkId = 1L;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookmarkController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("북마크_생성_성공")
    void 북마크_생성_성공() throws Exception {
        // Given
        doNothing().when(bookmarkService).bookmark(anyLong(), anyLong());

        // When & Then
        mockMvc.perform(post("/api/v1/bookmarks/{bakeryId}", bakeryId)
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("북마크 성공"))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크_취소_성공")
    void 북마크_취소_성공() throws Exception {
        // Given
        doNothing().when(bookmarkService).unbookmark(anyLong(), anyLong());

        // When & Then
        mockMvc.perform(delete("/api/v1/bookmarks/{bakeryId}", bakeryId)
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("북마크 취소 성공"))
                .andDo(print());
    }
}
