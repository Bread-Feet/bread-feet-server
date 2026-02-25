package kr.co.breadfeetserver.presentation.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import kr.co.breadfeetserver.application.review.ReviewService;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.infra.exception.GlobalExceptionHandler;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewController 단위 테스트")
class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ReviewUpdateRequest reviewUpdateRequest;
    private Long memberId = 1L;
    private Long reviewId = 1L;

    @BeforeEach
    void setUp() {
        // Custom Argument Resolver for @Memberid
        HandlerMethodArgumentResolver memberIdArgumentResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(Memberid.class)
                        && parameter.getParameterType().equals(Long.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter,
                    ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
                    throws Exception {
                // Simulate memberId being set in the request attribute, similar to how an interceptor would do it
                HttpServletRequest httpServletRequest = webRequest.getNativeRequest(
                        HttpServletRequest.class);
                httpServletRequest.setAttribute("memberId", memberId);
                return memberId;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setCustomArgumentResolvers(memberIdArgumentResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        reviewUpdateRequest = new ReviewUpdateRequest(
                "Updated content from controller",
                4.5,
                List.of(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg"
                )
        );
    }

    @Test
    @DisplayName("리뷰_수정_성공")
    void 리뷰_수정_성공() throws Exception {
        // Given
        doNothing().when(reviewService)
                .updateReview(anyLong(), anyLong(), any(ReviewUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/bakery/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("리뷰 수정 성공"));
    }

    @Test
    @DisplayName("리뷰_수정_실패_리뷰_없음")
    void 리뷰_수정_실패_리뷰_없음() throws Exception {
        // Given
        doThrow(new BreadFeetBusinessException(ErrorCode.REVIEW_NOT_FOUND))
                .when(reviewService)
                .updateReview(anyLong(), anyLong(), any(ReviewUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/bakery/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.code").value("REVIEW_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(ErrorCode.REVIEW_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("리뷰_수정_실패_권한_없음")
    void 리뷰_수정_실패_권한_없음() throws Exception {
        // Given
        doThrow(new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN))
                .when(reviewService)
                .updateReview(anyLong(), anyLong(), any(ReviewUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/bakery/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden()) // 403
                .andExpect(jsonPath("$.code").value("USER_NOT_ACCESS_FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        ErrorCode.USER_NOT_ACCESS_FORBIDDEN.getMessage()));
    }

    @Test
    @DisplayName("리뷰_삭제_성공")
    void 리뷰_삭제_성공() throws Exception {
        // Given
        doNothing().when(reviewService).deleteReview(anyLong(), anyLong());

        // When & Then
        mockMvc.perform(delete("/api/v1/bakery/reviews/{reviewId}", reviewId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("리뷰 삭제 성공"));
    }
}