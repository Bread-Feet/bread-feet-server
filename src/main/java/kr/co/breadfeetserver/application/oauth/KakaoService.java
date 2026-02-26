package kr.co.breadfeetserver.application.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@Slf4j
public class KakaoService {

    @Value("${spring.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.oauth2.kakao.client-secret}")
    private String clientSecret;

    // 1. 인가 코드를 받아서 '액세스 토큰'을 받아오는 메서드
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        log.debug("카카오 API 응답 상태: {}", response.getStatusCode());
        log.debug("카카오 API 응답 바디: {}", response.getBody());
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText();
    }

    // 2. 액세스 토큰으로 '유저 정보'를 가져오는 메서드
    public HashMap<String, Object> getUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );


        log.debug("카카오 API 응답 상태: {}", response.getStatusCode());
        log.debug("카카오 API 응답 바디: {}", response.getBody());
        // 응답(JSON) 파싱해서 필요한 정보(닉네임, 이메일 등) 꺼내기
        HashMap<String, Object> userInfo = new HashMap<>();
        try {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            //TODO: 나중에 추가하기 (이메일 등 등.. 카카오) String email = jsonNode.get("kakao_account").get("email").asText();
            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();

            userInfo.put("id", id);
            userInfo.put("nickname", nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}