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

}