package fr.eletutour.client;

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;
import feign.Response;

import java.util.Map;

public interface JasperFeignClient {
    @RequestLine("POST /reportExecutions")
    Response executeReport(@HeaderMap Map<String, Object> headerMap, String requestBody);

    @RequestLine("GET /reportExecutions/{requestId}/exports/{reportId}/status")
    Response status(@HeaderMap Map<String, Object> headerMap, @Param("requestId") String requestId, @Param("reportId") String reportId);

    @RequestLine("GET /reportExecutions/{requestId}/exports/{reportId}/outputResources")
    Response getReport(@HeaderMap Map<String, Object> headerMap, @Param("requestId") String requestId, @Param("reportId") String reportId);
}
