package fr.eletutour.client;

import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.response.execution.ExecutionResponse;

import java.util.List;
import java.util.Map;

public interface IJasperClient {

    byte[] getPDF(DocumentJasperRequest documentJasperRequest);

    ExecutionResponse execute(DocumentJasperRequest documentJasperRequest, List<String> cookies, Map<String, Object> headerMap);

    byte[] getReport(String rapportName, String requestId, String reportId, List<String> cookies, Map<String, Object> headerMap);
}
