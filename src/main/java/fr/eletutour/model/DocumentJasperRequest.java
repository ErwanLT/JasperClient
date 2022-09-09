package fr.eletutour.model;

public class DocumentJasperRequest {

    private String urlReport;
    private OutputFormat format;
    private ReportParameters parameters;

    public String getUrlReport() {
        return urlReport;
    }

    public void setUrlReport(String urlReport) {
        this.urlReport = urlReport;
    }

    public OutputFormat getFormat() {
        return format;
    }

    public void setFormat(OutputFormat format) {
        this.format = format;
    }

    public ReportParameters getParameters() {
        return parameters;
    }

    public void setParameters(ReportParameters parameters) {
        this.parameters = parameters;
    }
}
