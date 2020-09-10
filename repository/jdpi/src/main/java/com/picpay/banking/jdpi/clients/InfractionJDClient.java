package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPendingInfractionReportDTO;
import com.picpay.banking.jdpi.fallbacks.InfractionJDClientFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "infractionJDClient",
        url = "${pix.services.jdpi.dict.url}",
        path = "/jdpi/dict/api",
        fallbackFactory = InfractionJDClientFallbackFactory.class)
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface InfractionJDClient {

    @PostMapping("/v1/relato-infracao/incluir")
    CreateInfractionReportResponseDTO create(@RequestBody CreateInfractionReportRequestDTO request,
                                            @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @GetMapping("/v1/relato-infracao/listar/pendentes/{ispb}/{nrLimite}")
    ListPendingInfractionReportDTO listPendings(@PathVariable("ispb") Integer ispb, @PathVariable("nrLimite") Integer nrLimite);

}
