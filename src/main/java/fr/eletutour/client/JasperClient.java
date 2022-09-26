package fr.eletutour.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.ExecutionResponse;
import fr.eletutour.model.ReportParameters;
import fr.eletutour.model.exception.JasperClientException;
import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JasperClient implements IJasperClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperClient.class);


    private static final String REPORT_PARAMETER_NAME = "<reportParameter name=\"";
    private static final String REPORT_PARAMETER_CLOSE = "</reportParameter>";
    private static final String REGEX_TO_BASE64_STRING = ":\"[a-zA-Z\\d+]+\"";
    private static final String TEXT_FILE_TO_LOG = ":\"THIS IS BASE64 CONTENT...\"";

    protected final String user;
    protected final String password;
    protected final String jasperUrl;

    private WebClient client;
    private List<String> cookies;

    protected JasperClient(String user, String password, String jasperUrl) {
        this.user = user;
        this.password = password;
        this.jasperUrl = jasperUrl;
    }

    @PostConstruct
    private void init() {
        cookies = new ArrayList<>();

        ExchangeStrategies strategy = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(16*1024*1024))
                .build();

        client = WebClient.builder()
                .baseUrl(jasperUrl)
                .exchangeStrategies(strategy)
                .defaultHeaders(header -> header.setBasicAuth(user, password))
                .build();

    }

    @Override
    public byte[] getPDF(DocumentJasperRequest documentJasperRequest) {
        ExecutionResponse executionResponse = execute(documentJasperRequest);
        byte[] pdf = getReport(documentJasperRequest, executionResponse);
        if (pdf == null || pdf.length == 0) {
            throw new JasperClientException("The PDF is empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!cookies.isEmpty()){
            cookies.clear();
        }
        return pdf;
    }

    @Override
    public ExecutionResponse execute(DocumentJasperRequest documentJasperRequest){
        var headerSpec = client
                .post()
                .uri("/reportExecutions")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(buildXMLBody(documentJasperRequest)), String.class);

        var executionResponse = headerSpec.exchangeToMono(response -> {
            if(!response.statusCode().is2xxSuccessful()){
                throw new JasperClientException("Error while executing report", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                cookies.add(response.cookies().get("JSESSIONID").get(0).getValue());
                cookies.add(response.cookies().get("SERVERID").get(0).getValue());

                return response.bodyToMono(String.class);
            }
        });

        try {
            return new ObjectMapper().readValue(executionResponse.block(), ExecutionResponse.class);
        } catch (JsonProcessingException e) {
            throw new JasperClientException("Error while parsing execution response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] getReport(DocumentJasperRequest documentJasperRequest, ExecutionResponse executionResponse) {
        LOGGER.debug("Getting the PDF {} from jasper server", documentJasperRequest.getUrlReport());

        return client.get()
                .uri(jasperUrl+"/reportExecutions/"+executionResponse.getRequestId()+"/exports/"+executionResponse.getExports()[0].getId()+"/outputResource")
                .cookie("JSESSIONID", cookies.get(0))
                .cookie("SERVERID", cookies.get(1))
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(ByteArrayResource.class)
                .map(ByteArrayResource::getByteArray)
                .block();
    }

    private String buildXMLBody(DocumentJasperRequest documentJasperRequest) {
        LOGGER.debug("Build request body for report : {}", documentJasperRequest.getUrlReport());
        StringBuilder sb = new StringBuilder()
                .append("<reportExecutionRequest>")
                .append("<reportUnitUri>")
                .append(documentJasperRequest.getUrlReport())
                .append("</reportUnitUri>")
                .append("<async>false</async>")
                .append("<outputFormat>")
                .append(documentJasperRequest.getFormat().getFormat())
                .append("</outputFormat>")
                .append("<parameters>");

        String jsonString = buildJsonString(documentJasperRequest.getParameters(), documentJasperRequest.getUrlReport());

        sb.append(REPORT_PARAMETER_NAME).append("jsonString").append("\">")
                .append("<value>")
                .append(jsonString)
                .append("</value>")
                .append(REPORT_PARAMETER_CLOSE);

        sb.append("</parameters>")
                .append("</reportExecutionRequest>");

        String requestBody = sb.toString();
        String bodyToLog = requestBody.replaceAll(REGEX_TO_BASE64_STRING, TEXT_FILE_TO_LOG);
        LOGGER.debug("body of the request : {}", bodyToLog);

        return requestBody;
    }

    /**
     * Build of the JSON string as the single report parameter
     * @param reportParameter report parameters
     * @param reportUrl uri of the report
     * @return une chaine au format json
     */
    private String buildJsonString(ReportParameters reportParameter, String reportUrl) {


        Map<String, Object> parameters = new HashMap<>();

        simpleParameters(reportParameter.getDocumentJasperSimpleParameterList(), parameters, reportUrl);
        listParameters(reportParameter.getDocumentJasperListParameterList(), parameters, reportUrl);
        arrayParameters(reportParameter.getDocumentJasperArrayParameterList(), parameters, reportUrl);

        try {
            return new ObjectMapper().writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            throw new JasperClientException("Error while transforming parameter to JSON string", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    abstract void simpleParameters(List<DocumentJasperSimpleParameter> simpleParameters, Map<String, Object> parameters, String reportUrl);
    abstract void listParameters(List<DocumentJasperListParameter> listParameters, Map<String, Object> parameters, String reportUrl);
    abstract void arrayParameters(List<DocumentJasperArrayParameter> arrayParameters, Map<String, Object> parameters, String reportUrl);
}
