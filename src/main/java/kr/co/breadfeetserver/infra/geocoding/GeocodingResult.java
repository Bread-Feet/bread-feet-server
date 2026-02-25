package kr.co.breadfeetserver.infra.geocoding;

/**
 * 지번 주소를 좌표로 변환한 결과를 담는 불변 VO.
 *
 * <p>카카오 Local API 기준 좌표계 (WGS84):
 * <ul>
 *   <li>x = 경도 (longitude)</li>
 *   <li>y = 위도 (latitude)</li>
 * </ul>
 * MySQL ST_Distance_Sphere 함수도 POINT(경도, 위도) 순서를 사용하므로
 * 이 VO의 x, y 필드 순서와 일치한다.
 */
public record GeocodingResult(
        Double x,  // 경도 (longitude)
        Double y   // 위도 (latitude)
) {}
