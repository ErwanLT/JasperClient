package fr.eletutour.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import fr.eletutour.config.JasperClientImpl;
import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.OutputFormat;
import fr.eletutour.model.ReportParameters;
import fr.eletutour.model.exception.JasperClientException;
import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JasperClientTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();
        jasperClient = new JasperClientImpl("user_test", "password_test", "http://localhost:9561", new ObjectMapper());
    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }

    private JasperClient jasperClient;

    @Test
    public void testGetPDF() throws InterruptedException {
        wireMockServer.stubFor(
                post(urlEqualTo("/reportExecutions"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE+"; charset=utf-8")
                                .withHeader("set-cookie", "JSESSIONID=1111111111111; SERVERID=jasper")
                                .withBody("{\"status\":\"ready\",\"totalPages\":1,\"requestId\":\"217f7dc9-47c4-4c44-bada-7e29b653887b\",\"reportURI\":\"/test/test/test/test/export_test\",\"exports\":[{\"status\":\"ready\",\"outputResource\":{\"contentType\":\"application/pdf\",\"fileName\":\"export_test.pdf\",\"outputFinal\":true,\"outputTimestamp\":0},\"id\":\"6ca0038f-94ff-4bd9-bdf4-6a35259fd05e\"}]}")
                        )
        );

        wireMockServer.stubFor(
                get(urlMatching("/reportExecutions\\/[a-zA-Z0-9-]+\\/exports\\/[a-zA-Z0-9-]+\\/outputResource"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withBody("dummy pdf content".getBytes(StandardCharsets.UTF_8)))
        );
        
        byte[] pdf = jasperClient.getPDF(createDocumentJasperRequest());
        assertNotNull(pdf);
        assertEquals("dummy pdf content", new String(pdf, StandardCharsets.UTF_8));
       
    }
    
    @Test
    void testGetPDF_KO_executionError(){

        wireMockServer.stubFor(
                post(urlEqualTo("/reportExecutions"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE+"; charset=utf-8")
                                .withHeader("set-cookie", "JSESSIONID=1111111111111; SERVERID=jasper")
                        )
        );

        JasperClientException thrown = assertThrows(JasperClientException.class, () -> jasperClient.getPDF(createDocumentJasperRequest()));

        assertNotNull(thrown);
        assertEquals("Une erreur est survenue lors de la requete d'execution. : 500 INTERNAL_SERVER_ERROR", thrown.getMessage());
    }

    @Test
    void testGetPDF_KO_recuperationError() {
        wireMockServer.stubFor(
                post(urlEqualTo("/reportExecutions"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE+"; charset=utf-8")
                                .withHeader("set-cookie", "JSESSIONID=1111111111111; SERVERID=jasper")
                                .withBody("{\"status\":\"ready\",\"totalPages\":1,\"requestId\":\"217f7dc9-47c4-4c44-bada-7e29b653887b\",\"reportURI\":\"/test/test/test/test/export_test\",\"exports\":[{\"status\":\"ready\",\"outputResource\":{\"contentType\":\"application/pdf\",\"fileName\":\"export_test.pdf\",\"outputFinal\":true,\"outputTimestamp\":0},\"id\":\"6ca0038f-94ff-4bd9-bdf4-6a35259fd05e\"}]}")
                        )
        );

        wireMockServer.stubFor(
                get(urlMatching("/reportExecutions\\/[a-zA-Z0-9-]+\\/exports\\/[a-zA-Z0-9-]+\\/outputResource"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.NOT_FOUND.value())
                        )
        );

        JasperClientException thrown = assertThrows(JasperClientException.class, () -> jasperClient.getPDF(createDocumentJasperRequest()));

        assertNotNull(thrown);
        assertEquals("Une erreur est survenue lors de la récupération du rapport : 404 NOT_FOUND", thrown.getMessage());
    }

    private DocumentJasperRequest createDocumentJasperRequest() {
        DocumentJasperRequest request = new DocumentJasperRequest();
        request.setUrlReport("url_rapport_test");
        request.setFormat(OutputFormat.PDF);

        ReportParameters parameters = new ReportParameters();
        List<DocumentJasperSimpleParameter> simpleParameters = new ArrayList<>();
        List<DocumentJasperListParameter> listParameters = new ArrayList<>();
        List<DocumentJasperArrayParameter> arrayParameters = new ArrayList<>();

        DocumentJasperSimpleParameter sp = new DocumentJasperSimpleParameter()
                .withKey("Hello")
                .withValue("World !");
        simpleParameters.add(sp);

        DocumentJasperListParameter lp = new DocumentJasperListParameter()
                .withKey("Some")
                .withValues(Arrays.asList("parameters", "parameter 1", "parameter 2"));
        listParameters.add(lp);

        parameters.setDocumentJasperSimpleParameterList(simpleParameters);
        parameters.setDocumentJasperListParameterList(listParameters);
        parameters.setDocumentJasperArrayParameterList(arrayParameters);

        request.setParameters(parameters);
        return request;
    }


}
