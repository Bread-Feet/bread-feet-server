package kr.co.breadfeetserver.domain.bakery.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SortType;
import kr.co.breadfeetserver.presentation.bakery.dto.response.AddressResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class BakeryJdbcRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private BakeryJdbcRepository bakeryJdbcRepository;

    @BeforeEach
    void setUp() {
        bakeryJdbcRepository = new BakeryJdbcRepositoryImpl(namedParameterJdbcTemplate);
    }

    private BakeryListResponse createBakeryListResponse(Long id) {
        return new BakeryListResponse(
                id,
                "빵집 " + id,
                new AddressResponse("상세 주소 " + id, "지번 주소 " + id, "도로명 주소 " + id),
                "이미지 URL " + id,
                0L,
                0.0,
                false,
                false
        );
    }

    @Test
    @DisplayName("첫 페이지를 조회한다 (커서가 null).")
    void findFirstPage() {
        // given
        int size = 5;
        BakeryCursorCommand command = new BakeryCursorCommand(null, size, null, null,
                SortType.LATEST, false, false);

        List<BakeryListResponse> mockedData = new ArrayList<>();
        for (long i = 20; i >= 15; i--) {
            mockedData.add(createBakeryListResponse(i));
        }

        when(namedParameterJdbcTemplate.query(any(String.class), any(SqlParameterSource.class),
                any(BakeryRowMapper.class)))
                .thenReturn(mockedData);

        // when
        Slice<BakeryListResponse> result = bakeryJdbcRepository.findAll(command);

        // then
        assertThat(result.getContent()).hasSize(size);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.getContent().get(0).bakeryId()).isEqualTo(20L);
        assertThat(result.getContent().get(4).bakeryId()).isEqualTo(16L);
    }

    @Test
    @DisplayName("다음 페이지를 조회한다 (커서 사용).")
    void findNextPage() {
        // given
        int size = 5;
        Long cursor = 16L;
        BakeryCursorCommand command = new BakeryCursorCommand(cursor, size, null, null,
                SortType.LATEST, false, false);

        List<BakeryListResponse> mockedData = new ArrayList<>();
        for (long i = 15; i >= 10; i--) {
            mockedData.add(createBakeryListResponse(i));
        }

        when(namedParameterJdbcTemplate.query(any(String.class), any(SqlParameterSource.class),
                any(BakeryRowMapper.class)))
                .thenReturn(mockedData);

        // when
        Slice<BakeryListResponse> result = bakeryJdbcRepository.findAll(command);

        // then
        assertThat(result.getContent()).hasSize(size);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.getContent().get(0).bakeryId()).isEqualTo(15L);
        assertThat(result.getContent().get(4).bakeryId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("마지막 페이지를 조회한다.")
    void findLastPage() {
        // given
        int size = 5;
        Long cursor = 6L;
        BakeryCursorCommand command = new BakeryCursorCommand(cursor, size, null, null,
                SortType.LATEST, false, false);

        List<BakeryListResponse> mockedData = new ArrayList<>();
        for (long i = 5; i >= 1; i--) {
            mockedData.add(createBakeryListResponse(i));
        }

        when(namedParameterJdbcTemplate.query(any(String.class), any(SqlParameterSource.class),
                any(BakeryRowMapper.class)))
                .thenReturn(mockedData);

        // when
        Slice<BakeryListResponse> result = bakeryJdbcRepository.findAll(command);

        // then
        assertThat(result.getContent()).hasSize(size);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.getContent().get(0).bakeryId()).isEqualTo(5L);
        assertThat(result.getContent().get(4).bakeryId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("조회할 데이터가 페이지 크기보다 적은 경우를 테스트한다.")
    void findPartialLastPage() {
        // given
        int size = 5;
        Long cursor = 4L;
        BakeryCursorCommand command = new BakeryCursorCommand(cursor, size, null, null,
                SortType.LATEST, false, false);

        List<BakeryListResponse> mockedData = new ArrayList<>();
        for (long i = 3; i >= 1; i--) {
            mockedData.add(createBakeryListResponse(i));
        }

        when(namedParameterJdbcTemplate.query(any(String.class), any(SqlParameterSource.class),
                any(BakeryRowMapper.class)))
                .thenReturn(mockedData);

        // when
        Slice<BakeryListResponse> result = bakeryJdbcRepository.findAll(command);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.getContent().get(0).bakeryId()).isEqualTo(3L);
        assertThat(result.getContent().get(2).bakeryId()).isEqualTo(1L);
    }
}
