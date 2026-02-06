package kr.co.breadfeetserver.global.resolver;


import jakarta.servlet.http.HttpServletRequest;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Memberid.class) && parameter.getParameterType()
                .equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new BreadFeetBusinessException(ErrorCode.SERVER_ERROR);
        }

        Object memberId = request.getAttribute("memberId");

        // Check if memberId is required
        Memberid annotation = parameter.getParameterAnnotation(Memberid.class);
        if (annotation != null && annotation.required() && memberId == null) {
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return memberId;
    }
}
