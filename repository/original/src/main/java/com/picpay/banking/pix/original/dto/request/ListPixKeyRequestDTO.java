package com.picpay.banking.pix.original.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPixKeyRequestDTO {

    private List<PixKeyRequestDTO> listKey;

}
