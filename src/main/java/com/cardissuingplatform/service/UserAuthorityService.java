package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.request.ChangeAuthorityRequest;
import com.cardissuingplatform.model.Authority;
import com.cardissuingplatform.service.exception.AuthorityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthorityService {

    public void validateAuthority(ChangeAuthorityRequest changeAuthorityRequest) {

        List<ChangeAuthorityRequest.Authority> authorities = changeAuthorityRequest.getAuthorities();

        authorities.stream().filter(authority -> authority.name().equals(
                        Authority.AuthorityEnum.valueOf(authority.name()).name()))
                .findFirst()
                .orElseThrow(() -> new AuthorityNotFoundException("Authority not found"));

    }
}
