package com.cardissuingplatform.security;

import com.cardissuingplatform.controller.RestExceptionHandler;
import com.cardissuingplatform.service.UserProvider;
import com.cardissuingplatform.service.exception.AuthenticationException;
import com.cardissuingplatform.service.exception.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RestExceptionHandler restExceptionHandler;
    private final UserProvider userProvider;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException, JwtAuthenticationException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        try {
            if (token != null) {
                jwtTokenProvider.validateToken(token);

                if (!userProvider.checkEnabled(jwtTokenProvider.getUserId(token))) {
                    throw new AuthenticationException("User is disabled");
                }

                Authentication auth = jwtTokenProvider.getAuthentication(token);

                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);

        } catch (JwtAuthenticationException | AuthenticationException ex) {

            HttpServletResponse response = (HttpServletResponse) res;
            response.setStatus(HttpStatus.FORBIDDEN.value());

            response.getWriter().
                    write(restExceptionHandler.handleJwtAuthenticationException(ex.getMessage()).toString());
        }
    }
}
