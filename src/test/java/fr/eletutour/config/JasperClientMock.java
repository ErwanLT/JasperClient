package fr.eletutour.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.util.StreamUtils.copyToString;

public class JasperClientMock {

    public static void setupMockExecutionResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/reportExecutions"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE+"; charset=utf-8")
                        .withHeader("set-cookie", "JSESSIONID=1111111111111; SERVERID=jasper")
                        .withBody(
                                copyToString(
                                        JasperClientMock.class.getClassLoader().getResourceAsStream("payload/execution-response.json"),
                                        StandardCharsets.UTF_8))));
    }
}
