package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.AnalyzeInfractionReportDTO;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.request.FilterInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.AnalyzeResponseInfractionDTO;
import com.picpay.banking.jdpi.dto.response.CancelResponseInfractionDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListInfractionReportDTO;
import com.picpay.banking.jdpi.fallbacks.InfractionReportJDClientFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "infractionJDClient",
        url = "${pix.services.jdpi.infraction.url}",
        path = "/jdpi/dict/api",
        fallbackFactory = InfractionReportJDClientFallbackFactory.class)
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface InfractionReportJDClient {

    @PostMapping("/v1/relato-infracao/incluir")
    CreateInfractionReportResponseDTO create(@RequestBody CreateInfractionReportRequestDTO request,
                                            @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @GetMapping("/v1/relato-infracao/listar")
    ListInfractionReportDTO listPendings(@RequestParam("ispb") Integer ispb, @RequestParam("nrLimite") Integer nrLimite);

    @GetMapping("/v1/relato-infracao/consultar/{infractionReportId}")
    FindInfractionReportResponseDTO find(@PathVariable String infractionReportId);

    @PostMapping("/v1/relato-infracao/cancelar")
    CancelResponseInfractionDTO cancel(@RequestBody CancelInfractionDTO cancelInfractionDTO, @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @PostMapping("/v1/relato-infracao/analisar")
    AnalyzeResponseInfractionDTO analyze(@RequestBody AnalyzeInfractionReportDTO analyzeInfractionReportDTO, @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @PostMapping("/v1/relato-infracao/listar")
    ListInfractionReportDTO filter(@SpringQueryMap FilterInfractionReportDTO filter);

}
