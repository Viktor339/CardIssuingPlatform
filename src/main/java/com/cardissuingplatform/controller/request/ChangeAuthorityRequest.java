package com.cardissuingplatform.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class ChangeAuthorityRequest {

    @Positive
    private Long userId;
    @NotEmpty
    private List<Authority> authorities;


    public enum Authority{
        READ_INTERMEDIATE_CARD_STATUS,READ_CORPORATE_CARDS,READ_CARD_HISTORY
    }

}
