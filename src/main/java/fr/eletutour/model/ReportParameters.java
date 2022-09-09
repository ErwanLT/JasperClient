package fr.eletutour.model;

import fr.eletutour.model.parameter.DocumentJasperArrayParameter;
import fr.eletutour.model.parameter.DocumentJasperListParameter;
import fr.eletutour.model.parameter.DocumentJasperSimpleParameter;

import java.util.List;

public class ReportParameters {
    private List<DocumentJasperSimpleParameter> documentJasperSimpleParameterList;
    private List<DocumentJasperListParameter> documentJasperListParameterList;
    private List<DocumentJasperArrayParameter> documentJasperArrayParameterList;

    public List<DocumentJasperSimpleParameter> getDocumentJasperSimpleParameterList() {
        return documentJasperSimpleParameterList;
    }

    public List<DocumentJasperListParameter> getDocumentJasperListParameterList() {
        return documentJasperListParameterList;
    }

    public List<DocumentJasperArrayParameter> getDocumentJasperArrayParameterList() {
        return documentJasperArrayParameterList;
    }
}
