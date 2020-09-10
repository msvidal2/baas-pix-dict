package com.picpay.banking.pix.original.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ListPixKeyResponseDTO {

    private List<ListPixKeyDTO> response;
}