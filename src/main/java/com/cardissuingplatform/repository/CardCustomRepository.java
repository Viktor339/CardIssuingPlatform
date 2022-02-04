package com.cardissuingplatform.repository;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;

import java.util.List;

public interface CardCustomRepository {

    List<GetCardResponse> findByGetCardDto(GetCardDto getCardDto);
}
