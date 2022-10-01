package fr.eletutour.model.request.execution;

import java.util.Objects;

public class Parameters {
    private ReportParameter reportParameter;

    public Parameters(){}

    public Parameters reportParameters(ReportParameter reportParameter){
        this.setReportParameter(reportParameter);
        return this;
    }

    public ReportParameter getReportParameter() {
        return reportParameter;
    }

    public void setReportParameter(ReportParameter reportParameter) {
        this.reportParameter = reportParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameters that)) return false;
        return getReportParameter().equals(that.getReportParameter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReportParameter());
    }
}
