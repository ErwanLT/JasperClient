/*-
 * ========================LICENSE_START=================================
 * Jasper Client
 * ======================================================================
 * Copyright (C) 2022 Erwan Le Tutour
 * ======================================================================
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package fr.eletutour.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import feign.Feign;
import feign.Response;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.slf4j.Slf4jLogger;
import fr.eletutour.model.DocumentJasperRequest;
import fr.eletutour.model.ReportParameters;
import fr.eletutour.model.exception.JasperClientException;
import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;
import fr.eletutour.model.request.execution.Parameters;
import fr.eletutour.model.request.execution.ReportExecutionRequest;
import fr.eletutour.model.request.execution.ReportParameter;
import fr.eletutour.model.response.execution.ExecutionResponse;
import fr.eletutour.model.response.status.StatusResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Client ?? implementer par les projets pour appel au server jasper<br>
 * Pour r??cup??rer un rapport sur le server nous proc??dons en 3 ??tapes
 * <ol>
 *     <li>La demande d'??xecution du rapport avec les param??tres qui lui sont envoy??s</li>
 *     <li>la v??rification du statut de l'export avant r??cup??ration</li>
 *     <li>r??cup??ration du PDF g??n??r??</li>
 * </ol>
 * Chaque implementation du client a ?? sa charge l'implementation des m??thodes concernant la gestion des param??tres
 *
 * @author Erwan LE TUTOUR
 */
public abstract class JasperClient implements IJasperClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperClient.class);
    private static final String EXECUTION_ERROR = "Une erreur est survenue lors de la requete d'execution.";
    private static final String RECUPERATION_ERROR = "Une erreur est survenue lors de la r??cup??ration du rapport";
    private static final String EXECUTION_ERROR_REPORT = EXECUTION_ERROR + "pour le rapport {}.";
    private static final String COOKIES_ERROR = "Une erreur est survenue lors de la r??cup??ration des cookies.";
    private static final String EXECUTION_STEP = "execution";
    private static final String STATUS_STEP = "check status";
    private static final String RECUPERATION_STEP = "recuperation";
    public static final String CONTENT_TYPE = "Content-Type";
    private static final String STATUS_ERROR = "Error while checking status.";
    protected final String user;
    protected final String password;
    protected final String jasperUrl;
    protected final ObjectMapper mapper;
    private JasperFeignClient jasperFeignClient;

    protected JasperClient(String user, String password, String jasperUrl, ObjectMapper mapper) {
        this.user = user;
        this.password = password;
        this.jasperUrl = jasperUrl;
        this.mapper = mapper;

        init();
    }

    /**
     * initialisation du client feign pour les requete HTTP avec les information pass??e en param??tres du constructeur
     */
    private void init() {
        Feign.Builder builder = Feign.builder()
                .requestInterceptor(new BasicAuthRequestInterceptor(user,
                password))
                .logger(new Slf4jLogger(JasperFeignClient.class))
                .logLevel(feign.Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY);
        jasperFeignClient = builder
                .target(JasperFeignClient.class, jasperUrl);

    }

    /**
     * Methode principale du client pour la generation et la r??cup??ration d'un rapport il faut :
     * <ol>
     *     <li>Demander l'??xecution du rapport</li>
     *     <li>Demander la r??cup??ration de la ressource g??n??r??e</li>
     * </ol>
     *
     * @param documentJasperRequest la requete pour le server jasper
     * @return un fichier PDF
     */
    @Override
    public byte[] getPDF(DocumentJasperRequest documentJasperRequest) throws InterruptedException {
        Map<String,Object> headerMap = initHeaderMap();
        List<String> cookies = new ArrayList<>();
        ExecutionResponse executionResponse = execute(documentJasperRequest, cookies, headerMap);
        boolean exportReady = executionResponse.getExports()[0].getStatus().equals("ready");
        while (!exportReady){
            TimeUnit.MILLISECONDS.sleep(100L);
            exportReady = checkExportStatus(executionResponse.getRequestId(), executionResponse.getExports()[0].getId(), cookies, headerMap);
        }
        byte[] pdf = getReport(documentJasperRequest.getUrlReport(), executionResponse.getRequestId(), executionResponse.getExports()[0].getId(), cookies, headerMap);
        if (pdf == null || pdf.length == 0) {
            throw new JasperClientException("The PDF is empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return pdf;
    }

    /**
     * V??rification du statut d'un rapport avant r??cup??ration<br>
     * Certains rapport pouvant ??tres assez volumineux et long ?? g??n??rer il est essentiel de v??rifier le statut
     * de ce dernier avant d'essayer de le r??cup??rer
     * @param executionResponse la r??ponse de la requete d'execution
     * @param cookies les cookies n??cessaire ?? l'execution de la requets
     * @param headerMap les headers de la requ??te
     * @return <ol>
     *     <li>true : export pret ?? ??tre r??cup??r??</li>
     *     <li>false : il faut encore attendre avant de r??cup??rer l'export</li>
     * </ol>
     */
    private Boolean checkExportStatus(String requestId, String reportId, List<String> cookies, Map<String, Object> headerMap) {
        headerMap.put("Cookie", StringUtils.join(cookies, ";"));
        headerMap.remove(CONTENT_TYPE);

        try (Response r = jasperFeignClient.status(headerMap, requestId, reportId)) {

            checkResponseStatut(r.status(), STATUS_STEP, null, null);

            try {
                String statusResponse = IOUtils.toString(r.body().asInputStream(), String.valueOf(StandardCharsets.UTF_8));
                StatusResponse status = new ObjectMapper().readValue(statusResponse, StatusResponse.class);
                if ("ready".equals(status.getValue())) {
                    return true;
                }
            } catch (IOException e) {
                throw new JasperClientException(STATUS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return false;
    }

    /**
     * initialisation des headers ?? mettre dans les requ??tes vers le server jasper
     * @return une map avec les valeurs des diff??rents headers
     */
    private Map<String, Object> initHeaderMap() {
        Map<String,Object> headerMap = new HashMap<>();
        headerMap.put("Accept-Encoding", "gzip, deflate, br");
        headerMap.put("Connection", "keep-alive");
        return headerMap;
    }

    /**
     * M??thode demandant l'??xecution d'un rapport jasper avec les param??tre qu'on lui fournit<br>
     * Lors de cette requete en plus de la r??ponse d'execution {@link ExecutionResponse} nous recevons des cookies qu'il nous faut garder
     * <ol>
     *     <li>JSESSIONID</li>
     *     <li>SERVERID</li>
     * </ol>
     * Ces cookies sont n??cessaire afin d'int??roger le server plus tard pour les information de statut et la r??cup??ration du PDF
     * @param documentJasperRequest les informations concernant le rapport (url sur le server param??tres ?? lui fournir ...)
     * @param cookies les cookies n??cessaire ?? l'execution de la requete
     * @param headerMap les headers de la requ??te
     * @return {@link ExecutionResponse}
     */
    public ExecutionResponse execute(DocumentJasperRequest documentJasperRequest, List<String> cookies, Map<String, Object> headerMap) {
        LOGGER.info("Demande d'execution du rapport : {}", documentJasperRequest.getUrlReport());

        String requestBody = buildXMLBody(documentJasperRequest);
        headerMap.put(CONTENT_TYPE, "application/xml");
        headerMap.put("Accept", "application/json");
        feign.Response r = jasperFeignClient.executeReport(headerMap, requestBody);

        checkResponseStatut(r.status(), EXECUTION_STEP, documentJasperRequest.getUrlReport(), requestBody);
        getCookie(r, cookies);

        try {
            String execResponse = IOUtils.toString(r.body().asInputStream(), String.valueOf(StandardCharsets.UTF_8));
            Gson g = new Gson();
            return g.fromJson(execResponse, ExecutionResponse.class);
        } catch (IOException e) {
            throw new JasperClientException("Erreur lors du parsing de la r??ponse de la requete d'execution", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * R??cup??ration des cookies de la r??ponse d'??xecution<br>
     * Ces cookies sont obligatoire pour les prochaines requ??tes vers le server
     * @param r la r??ponse de la requ??te d'??xecution du rapport
     * @param cookies la liste des cookies
     */
    private void getCookie(Response r, List<String> cookies) {
        Collection<String> responseCookies = r.headers().get("set-cookie");
        if(responseCookies == null || responseCookies.isEmpty()){
            throw new JasperClientException(COOKIES_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        cookies.addAll(responseCookies);
    }


    /**
     * M??thode de r??cup??ration du PDF pr??c??dement g??n??r??
     * @param rapportName l'url du rapport sur le server jasper
     * @param requestId l'Id de la requete
     * @param reportId l'id de la ressource g??n??r??e
     * @param cookies les cookies n??cessaire ?? l'execution de la requete
     * @param headerMap les headers de la requ??te
     * @return le PDF
     */
    @Override
    public byte[] getReport(String rapportName, String requestId, String reportId, List<String> cookies, Map<String, Object> headerMap) {
        LOGGER.info("R??cup??ration du rapport : {}", rapportName);

        headerMap.put("Cookie", StringUtils.join(cookies, ";"));
        headerMap.remove(CONTENT_TYPE);

        feign.Response r = jasperFeignClient.getReport(headerMap, requestId, reportId);

        checkResponseStatut(r.status(), RECUPERATION_STEP, rapportName, null);

        try {
            return r.body().asInputStream().readAllBytes();
        } catch (IOException e) {
            throw new JasperClientException(RECUPERATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Construction du body de la requ??te d'??xection du rapport
     * @param documentJasperRequest les informations relatives au rapport
     * @return une chaine XML
     */
    private String buildXMLBody(DocumentJasperRequest documentJasperRequest) {
        LOGGER.info("Construction corps de la requete pour le rapport : {}", documentJasperRequest.getUrlReport());
        ReportExecutionRequest reportExecutionRequest = new ReportExecutionRequest()
                .reportUnitUri(documentJasperRequest.getUrlReport())
                .asyn(Boolean.FALSE)
                .outputFormat(documentJasperRequest.getFormat().getFormat())
                .parameters(new Parameters()
                        .reportParameters(new ReportParameter()
                                .value(buildJsonString(documentJasperRequest.getParameters(), documentJasperRequest.getUrlReport()))));

        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReportExecutionRequest.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(reportExecutionRequest, sw);
        } catch (JAXBException e) {
            throw new JasperClientException("Erreur lors de la cr??ation du corps de la requete", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return sw.toString();
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

    /**
     * Check du statut de la r??ponse retourn?? par le server jasper
     * @param statusCode le statut de la r??ponse
     * @param step le step de la r??ponse
     * @param fileName le nom du rapport
     * @param requestBody la corps de la requete envoy?? au server
     */
    private void checkResponseStatut(int statusCode, String step, String fileName, String requestBody){
        if(statusCode != 200){
            switch (step) {
                case EXECUTION_STEP :
                    LOGGER.error(EXECUTION_ERROR_REPORT, fileName);
                    LOGGER.error("corps de la requete en erreur : {}", requestBody);
                    throw new JasperClientException(EXECUTION_ERROR + " : " + HttpStatus.resolve(statusCode), HttpStatus.INTERNAL_SERVER_ERROR);
                case RECUPERATION_STEP :
                    LOGGER.error(RECUPERATION_ERROR);
                    throw new JasperClientException(RECUPERATION_ERROR + " : " + HttpStatus.resolve(statusCode), HttpStatus.INTERNAL_SERVER_ERROR);
                case STATUS_STEP :
                    LOGGER.error(STATUS_ERROR);
                    throw new JasperClientException(STATUS_ERROR + " : " + HttpStatus.resolve(statusCode), HttpStatus.INTERNAL_SERVER_ERROR);
                default : throw new IllegalArgumentException("Step non reconnu.");
            }
        }
    }

    public abstract void simpleParameters(List<DocumentJasperSimpleParameter> simpleParameters, Map<String, Object> parameters, String reportUrl);
    public abstract void listParameters(List<DocumentJasperListParameter> listParameters, Map<String, Object> parameters, String reportUrl);
    public abstract void arrayParameters(List<DocumentJasperArrayParameter> arrayParameters, Map<String, Object> parameters, String reportUrl);
}
