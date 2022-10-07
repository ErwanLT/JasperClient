package fr.eletutour.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.eletutour.config.JasperClientImpl;
import fr.eletutour.config.JasperClientMock;
import fr.eletutour.config.WireMockConfig;
import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.OutputFormat;
import fr.eletutour.model.ReportParameters;
import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class JasperClientTest {

    @Autowired
    private WireMockServer mockJasperClient;

    private JasperClient jasperClient;

    @BeforeEach
    void setUp() throws IOException {
        JasperClientMock.setupMockExecutionResponse(mockJasperClient);
        jasperClient = new JasperClientImpl("user_test", "password_test", "http://localhost:9561");
    }

    @Test
    public void testGetPDF(){
        assertNotNull(jasperClient.getPDF(createDocumentJasperRequest()));
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
