package com.picpay.banking.pix.original.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OriginalErrorDTO {

    private String status;

    private String error;

    private List<OriginalFieldErroDTO> errors;
}
