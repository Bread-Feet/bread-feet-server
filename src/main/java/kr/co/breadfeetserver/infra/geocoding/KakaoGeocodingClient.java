package kr.co.breadfeetserver.infra.geocoding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 카카오 Local API를 이용해 지번 주소를 WGS84 좌표(경도/위도)로 변환하는 클라이언트.
 *
 * <p>사용 API: 카카오 주소 검색 API
 * <pre>GET https://dapi.kakao.com/v2/local/search/address.json?query={address}</pre>
 *
 * <p>응답 문서 중 첫 번째 결과의 x(경도), y(위도)를 사용한다.
 * 변환 실패(주소 미존재, 네트워크 오류 등)는 예외를 전파하지 않고 {@code Optional.empty()}를 반환한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoGeocodingClient {

    private static final String KAKAO_LOCAL_API_URL =
            "https://dapi.kakao.com/v2/local/search/address.json";

    /**
     * 카카오 REST API 키. OAuth 인증과 동일한 키를 재사용한다.
     * Authorization 헤더: "KakaoAK {REST_API_KEY}"
     */
    @Value("${spring.oauth2.kakao.client-id}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 지번 주소를 카카오 Local API를 통해 WGS84 좌표로 변환한다.
     *
     * <p>좌표가 이미 DB에 저장된 빵집은 이 메서드를 호출하지 않아도 된다.
     * 좌표가 없는 빵집의 {@code lot_number}를 채울 때 사용한다.
     *
     * @param lotNumber 지번 주소 (예: "서울 마포구 서교동 364-12")
     * @return 변환 성공 시 {@link GeocodingResult}, 실패 시 {@code Optional.empty()}
     */
    public Optional<GeocodingResult> geocode(String lotNumber) {
        try {
            URI uri = buildUri(lotNumber);
            HttpHeaders headers = buildAuthHeaders();
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            return parseCoordinates(response.getBody());

        } catch (Exception e) {
            // 외부 API 오류는 예외를 전파하지 않고 로그만 남긴다.
            // 좌표 없는 빵집이 결과에서 제외될 뿐, 서비스 전체 장애로 이어지지 않는다.
            log.warn("지번 주소 → 좌표 변환 실패. address={}, error={}", lotNumber, e.getMessage());
            return Optional.empty();
        }
    }

    // ========== private ==========

    private URI buildUri(String lotNumber) {
        return UriComponentsBuilder
                .fromUriString(KAKAO_LOCAL_API_URL)
                .queryParam("query", lotNumber)
                .build()
                .encode()
                .toUri();
    }

    private HttpHeaders buildAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        return headers;
    }

    /**
     * 카카오 API 응답 JSON에서 첫 번째 문서의 좌표를 추출한다.
     *
     * <p>응답 형식:
     * <pre>
     * {
     *   "documents": [{ "x": "126.978...", "y": "37.566..." }, ...]
     * }
     * </pre>
     */
    private Optional<GeocodingResult> parseCoordinates(String responseBody) throws Exception {
        JsonNode documents = objectMapper.readTree(responseBody).path("documents");

        if (documents.isEmpty()) {
            return Optional.empty();
        }

        JsonNode first = documents.get(0);
        double x = first.get("x").asDouble(); // 경도
        double y = first.get("y").asDouble(); // 위도

        return Optional.of(new GeocodingResult(x, y));
    }
}
