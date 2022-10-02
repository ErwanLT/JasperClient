package fr.eletutour.model.response.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class StatusResponse {
    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StatusResponse(@JsonProperty("status") String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusResponse status)) return false;
        return Objects.equals(value, status.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "status{" +
                "value='" + value + '\'' +
                '}';
    }
}
