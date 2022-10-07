package fr.eletutour.config;

import fr.eletutour.client.JasperClient;
import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class JasperClientImpl extends JasperClient {

    public JasperClientImpl(String user, String password, String jasperUrl) {
        super(user, password, jasperUrl);
    }

    @Override
    public void simpleParameters(List<DocumentJasperSimpleParameter> simpleParameters, Map<String, Object> parameters, String reportUrl) {
        for(DocumentJasperSimpleParameter sp : simpleParameters){
            parameters.put(sp.getKey(), sp.getValue());
        }
    }

    @Override
    public void listParameters(List<DocumentJasperListParameter> listParameters, Map<String, Object> parameters, String reportUrl) {
        for (DocumentJasperListParameter lp : listParameters){
            String val = StringUtils.join(parameters.get(lp.getKey()), ";");
            parameters.put(lp.getKey(), val);
        }
    }

    @Override
    public void arrayParameters(List<DocumentJasperArrayParameter> arrayParameters, Map<String, Object> parameters, String reportUrl) {

    }
}
