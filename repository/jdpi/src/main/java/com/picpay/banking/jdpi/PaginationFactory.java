package com.picpay.banking.jdpi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.common.Pagination;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationFactory {

    private static final String PAGINATION_HEADER = "X-Paginacao";
    private static final String TOTAL_RECORDS_PARAM = "totalRegistros";
    private static final String CURRENT_PAGE_PARAM = "paginaAtual";
    private static final String PAGE_SIZE_PARAM = "tamanhoPagina";
    private static final String HAS_PREVIOUS_PARAM = "temPaginaAnterior";
    private static final String HAS_NEXT_PARAM = "temProximaPagina";

    public static <T> Pagination<T> create(HttpHeaders httpHeaders, T result) {
        Pagination<T> pagination = new Pagination<>();
        try {
            String jsonPagination = httpHeaders.get(PAGINATION_HEADER).get(0);
            JsonNode json = new ObjectMapper().readTree(jsonPagination);
            pagination.setResult(result);
            pagination.setTotalRecords(json.get(TOTAL_RECORDS_PARAM).asInt());
            pagination.setCurrentPage(json.get(CURRENT_PAGE_PARAM).asInt());
            pagination.setPageSize(json.get(PAGE_SIZE_PARAM).asInt());
            pagination.setHasPrevious(json.get(HAS_PREVIOUS_PARAM).asBoolean());
            pagination.setHasNext(json.get(HAS_NEXT_PARAM).asBoolean());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return pagination;
    }

}