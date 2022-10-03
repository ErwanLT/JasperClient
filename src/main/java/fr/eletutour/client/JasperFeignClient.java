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

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;
import feign.Response;

import java.util.Map;

public interface JasperFeignClient {
    @RequestLine("POST /reportExecutions")
    Response executeReport(@HeaderMap Map<String, Object> headerMap, String requestBody);

    @RequestLine("GET /reportExecutions/{requestId}/exports/{reportId}/status")
    Response status(@HeaderMap Map<String, Object> headerMap, @Param("requestId") String requestId, @Param("reportId") String reportId);

    @RequestLine("GET /reportExecutions/{requestId}/exports/{reportId}/outputResources")
    Response getReport(@HeaderMap Map<String, Object> headerMap, @Param("requestId") String requestId, @Param("reportId") String reportId);
}
