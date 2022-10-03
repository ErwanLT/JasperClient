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
package fr.eletutour.model.response.execution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public final class ExecutionResponse {
    private final String status;
    private final int totalPages;
    private final String requestId;
    private final String reportURI;
    private final Export[] exports;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ExecutionResponse(@JsonProperty("status") String status,
                             @JsonProperty("totalPages")int totalPages,
                             @JsonProperty("requestId") String requestId,
                             @JsonProperty("reportURI") String reportURI,
                             @JsonProperty("exports") Export[] exports) {
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
