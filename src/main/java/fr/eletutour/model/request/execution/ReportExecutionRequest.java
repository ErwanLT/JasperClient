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
