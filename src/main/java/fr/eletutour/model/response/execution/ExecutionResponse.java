package fr.eletutour.model.response.execution;

import java.util.Arrays;
import java.util.Objects;

public final class ExecutionResponse {
    private final String status;
    private final int totalPages;
    private final String requestId;
    private final String reportURI;
    private final Export[] exports;

    public ExecutionResponse(String status, int totalPages, String requestId, String reportURI, Export[] exports) {
        this.status = status;
        this.totalPages = totalPages;
        this.requestId = requestId;
        this.reportURI = reportURI;
        this.exports = exports;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getReportURI() {
        return reportURI;
    }

    public Export[] getExports() {
        return exports;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionResponse)) return false;
        ExecutionResponse that = (ExecutionResponse) o;
        return getTotalPages() == that.getTotalPages() && getStatus().equals(that.getStatus()) && getRequestId().equals(that.getRequestId()) && getReportURI().equals(that.getReportURI()) && Arrays.equals(getExports(), that.getExports());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getStatus(), getTotalPages(), getRequestId(), getReportURI());
        result = 31 * result + Arrays.hashCode(getExports());
        return result;
    }
}
