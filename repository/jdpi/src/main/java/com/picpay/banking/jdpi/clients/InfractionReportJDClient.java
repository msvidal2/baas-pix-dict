package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.AnalyzeInfractionReportDTO;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.request.FilterInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.*;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "infractionJDClient",
        url = "${pix.services.jdpi.infraction.url}",
        path = "/jdpi/dict/api")
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface InfractionReportJDClient {

    @PostMapping("/v1/relato-infracao/incluir")
    CreateInfractionReportResponseDTO create(@RequestBody CreateInfractionReportRequestDTO request,
                                            @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @GetMapping("/v1/relato-infracao/listar/pendentes")
    ListInfractionReportDTO listPending(@RequestParam("ispb") Integer ispb, @RequestParam("nrLimite") Integer nrLimite);

    @GetMapping("/v1/relato-infracao/consultar")
    FindInfractionReportResponseDTO find(@RequestParam("ispb") Integer ispb, @RequestParam String idRelatoInfracao);

    @PostMapping("/v1/relato-infracao/cancelar")
    CancelResponseInfractionDTO cancel(@RequestBody CancelInfractionDTO cancelInfractionDTO, @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @PostMapping("/v1/relato-infracao/analisar")
    AnalyzeResponseInfractionDTO analyze(@RequestBody AnalyzeInfractionReportDTO analyzeInfractionReportDTO, @RequestHeader("Chave-Idempotencia") String requestIdentifier);

    @GetMapping("/v1/relato-infracao/listar")
    ListInfractionReportDTO filter(@SpringQueryMap FilterInfractionReportDTO filter);

}
