package fr.eletutour.model;

import java.util.Arrays;

public class ExecutionResponse {
    private String status;
    private int totalPages;
    private String requestId;
    private String reportURI;
    private Export[] exports;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReportURI() {
        return reportURI;
    }

    public void setReportURI(String reportURI) {
        this.reportURI = reportURI;
    }

    public Export[] getExports() {
        return exports;
    }

    public void setExports(Export[] exports) {
        this.exports = exports;
    }

    @Override
    public String toString() {
        return "ExecutionResponse{" +
                "status='" + status + '\'' +
                ", totalPages=" + totalPages +
                ", requestId='" + requestId + '\'' +
                ", reportURI='" + reportURI + '\'' +
                ", exports=" + Arrays.toString(exports) +
                '}';
    }
}
