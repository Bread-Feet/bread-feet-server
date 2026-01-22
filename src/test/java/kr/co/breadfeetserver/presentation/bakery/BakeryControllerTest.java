package kr.co.breadfeetserver.presentation.bakery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.breadfeetserver.application.bakery.BakeryService;
import kr.co.breadfeetserver.fixture.BakeryFixture;
import kr.co.breadfeetserver.infra.exception.GlobalExceptionHandler;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("BakeryController 단위 테스트")
class BakeryControllerTest {

    @InjectMocks
    private BakeryController bakeryController;

    @Mock
    private BakeryService bakeryService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BakeryCreateRequest bakeryCreateRequest;
    private BakeryUpdateRequest bakeryUpdateRequest;
    private final Long memberId = 1L;
    private final Long bakeryId = 1L;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bakeryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        bakeryCreateRequest = BakeryFixture.aBakeryCreateRequest();
        bakeryUpdateRequest = BakeryFixture.aBakeryUpdateRequest(bakeryId);
    }

    @Test
    @DisplayName("빵집_생성_성공")
    void 빵집_생성_성공() throws Exception {
        // Given
        when(bakeryService.createBakery(anyLong(), any(BakeryCreateRequest.class))).thenReturn(
                bakeryId);

        // When & Then
        mockMvc.perform(post("/api/v1/bakeries")
                        .param("memberId", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bakeryCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(bakeryId))
                .andDo(print());
    }

    @Test
    @DisplayName("빵집_수정_성공")
    void 빵집_수정_성공() throws Exception {
        // Given
        doNothing().when(bakeryService)
                .updateBakery(anyLong(), anyLong(), any(BakeryUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/bakeries/{id}", bakeryId)
                        .param("memberId", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bakeryUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("빵집_삭제_성공")
    void 빵집_삭제_성공() throws Exception {
        // Given
        doNothing().when(bakeryService).deleteBakery(anyLong(), anyLong());

        // When & Then
        mockMvc.perform(delete("/api/v1/bakeries/{id}", bakeryId)
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
