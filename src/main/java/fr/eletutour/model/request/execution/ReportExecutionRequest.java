package fr.eletutour.model.request.execution;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name="reportExecutionRequest")
public class ReportExecutionRequest {
    private String reportUnitUri;
    private Boolean async;
    private String outputFormat;
    private Parameters parameters;

    public ReportExecutionRequest() {
    }

    public ReportExecutionRequest reportUnitUri(String reportUnitUri){
        this.setReportUnitUri(reportUnitUri);
        return this;
    }

    public ReportExecutionRequest asyn(Boolean async){
        this.setAsync(async);
        return this;
    }

    public ReportExecutionRequest outputFormat(String outputFormat){
        this.setOutputFormat(outputFormat);
        return this;
    }

    public ReportExecutionRequest parameters(Parameters parameters){
        this.setParameters(parameters);
        return this;
    }

    public String getReportUnitUri() {
        return reportUnitUri;
    }

    public void setReportUnitUri(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportExecutionRequest that)) return false;
        return getReportUnitUri().equals(that.getReportUnitUri()) && getAsync().equals(that.getAsync()) && getOutputFormat().equals(that.getOutputFormat()) && getParameters().equals(that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReportUnitUri(), getAsync(), getOutputFormat(), getParameters());
    }
}
