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

import java.util.Objects;

public final class Export {
    private final String status;
    private final String id;
    private final OutputResource resource;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Export(@JsonProperty("status") String status,
                  @JsonProperty("id") String id,
                  @JsonProperty("resource") OutputResource resource) {
        this.status = status;
        this.id = id;
        this.resource = resource;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public OutputResource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "Export{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", resource=" + resource +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Export)) return false;
        Export export = (Export) o;
        return getStatus().equals(export.getStatus()) && getId().equals(export.getId()) && getResource().equals(export.getResource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getId(), getResource());
    }
}
