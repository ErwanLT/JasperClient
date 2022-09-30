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
