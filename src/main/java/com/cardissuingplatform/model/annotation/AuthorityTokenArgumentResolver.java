package com.cardissuingplatform.model.annotation;

import com.cardissuingplatform.controller.dto.TokenDto;
import com.cardissuingplatform.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AuthorityTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(Token.class) != null;
    }

    @Override
    public TokenDto resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request
                = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        String token = request.getHeader("Authorization").substring(7);

        return TokenDto.builder()
                .roles(jwtTokenProvider.getRoles(token))
                .userId(jwtTokenProvider.getUserId(token))
                .build();
    }
}
