package com.cardissuingplatform.controller;

import com.cardissuingplatform.config.PageProperties;
import com.cardissuingplatform.controller.dto.TokenDto;
import com.cardissuingplatform.controller.request.ChangeAuthorityRequest;
import com.cardissuingplatform.controller.response.ChangeAuthorityResponse;
import com.cardissuingplatform.controller.response.GetAuthorityResponse;
import com.cardissuingplatform.model.annotation.Token;
import com.cardissuingplatform.service.AuthorityService;
import com.cardissuingplatform.service.PageService;
import com.cardissuingplatform.service.UserAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accountant/v1/users/authorities")
@PreAuthorize("hasRole('ACCOUNTANT')")
public class AuthorityController {
    private final AuthorityService authorityService;
    private final UserAuthorityService userAuthorityService;
    private final PageProperties pageProperties;
    private final PageService pageService;


    @PutMapping
    public ChangeAuthorityResponse change(@Valid @RequestBody ChangeAuthorityRequest changeAuthorityRequest, @Token TokenDto token) {

        userAuthorityService.validateAuthority(changeAuthorityRequest);
        return authorityService.change(changeAuthorityRequest, token);

    }

    @GetMapping
    public List<GetAuthorityResponse> get(@RequestParam(name = "size") Integer size,
                                          @RequestParam("page") Integer page, @Token TokenDto token) {

        Integer validatedSize = pageService.validatePageSize(size, pageProperties.getMin(), pageProperties.getMax());
        return authorityService.get(validatedSize, page, token);
    }

}
