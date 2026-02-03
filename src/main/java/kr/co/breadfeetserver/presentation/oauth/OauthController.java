package kr.co.breadfeetserver.presentation.oauth;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.breadfeetserver.application.oauth.KakaoService;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.member.MemberRole;
import kr.co.breadfeetserver.infra.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class OauthController {

    private final KakaoService kakaoService;
    private final MemberJpaRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.oauth2.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @GetMapping("/oauth/kakao/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String authUrl =
                "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId + "&redirect_uri="
                        + kakaoRedirectUri + "&response_type=code";
        response.sendRedirect(authUrl);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

        // 1. 인가 코드로 엑세스 토큰 받아오기
        String accessToken = kakaoService.getAccessToken(code);

        // 2. 토큰으로 카카오 사용자 정보 가져오기
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        Long kakaoId = (Long) userInfo.get("id");
        String nickname = (String) userInfo.get("nickname");

        // 3. DB 조회 및 저장 (빌더 패턴 사용)
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .kakaoId(kakaoId)
                            .nickname(nickname)
                            .email(kakaoId + "@kakao.user")
                            .role(MemberRole.USER)
                            .build();
                    return memberRepository.save(newMember);
                });

        String jwtToken = jwtUtil.createToken(member.getId(), member.getNickname());
        response.sendRedirect("http://localhost:3000/oauth/callback?token=" + jwtToken);
    }
}