package fr.eletutour.client;

import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.ExecutionResponse;

public interface IJasperClient {

    byte[] getPDF(DocumentJasperRequest documentJasperRequest);

    ExecutionResponse execute(DocumentJasperRequest documentJasperRequest);

    byte[] getReport(DocumentJasperRequest documentJasperRequest, ExecutionResponse executionResponse);
}
