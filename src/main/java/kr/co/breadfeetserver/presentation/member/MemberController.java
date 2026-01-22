package kr.co.breadfeetserver.presentation.member;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.breadfeetserver.application.oauth.KakaoService;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.member.MemberRole;
import kr.co.breadfeetserver.infra.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KakaoService kakaoService;
    private final MemberJpaRepository memberRepository;
    private final JwtUtil jwtUtil;

    // [중요] 카카오 개발자 센터의 'Redirect URI'와 이 주소가 토씨 하나 안 틀리고 똑같아야 합니다!
    // 보통 표준인 '/login/oauth2/code/kakao'를 많이 씁니다.
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
                            .email(" ")
                            .role(MemberRole.USER)
                            .build();
                    return memberRepository.save(newMember);
                });

        // 4. JWT 토큰 발급
        String jwtToken = jwtUtil.createToken(member.getId(), member.getNickname());

        // 5. 프론트엔드로 리다이렉트
        // 프론트엔드 개발자와 맞춘 주소로 보내주세요.
        // 예: http://localhost:3000/oauth/callback?token=...
        response.sendRedirect("http://localhost:3000/oauth/callback?token=" + jwtToken);
    }
}