package fr.eletutour.model.response.execution;

import java.util.Objects;

public final class OutputResource {
    private final String contentType;
    private final String fileName;
    private final Boolean outputFinal;
    private final Long outputTimestamp;

    public OutputResource(String contentType, String fileName, Boolean outputFinal, Long outputTimestamp) {
        this.contentType = contentType;
        this.fileName = fileName;
        this.outputFinal = outputFinal;
        this.outputTimestamp = outputTimestamp;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getOutputFinal() {
        return outputFinal;
    }

    public Long getOutputTimestamp() {
        return outputTimestamp;
    }

    @Override
    public String toString() {
        return "OutputResource{" +
                "contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", outputFinal=" + outputFinal +
                ", outputTimestamp=" + outputTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutputResource)) return false;
        OutputResource that = (OutputResource) o;
        return getContentType().equals(that.getContentType()) && getFileName().equals(that.getFileName()) && getOutputFinal().equals(that.getOutputFinal()) && getOutputTimestamp().equals(that.getOutputTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContentType(), getFileName(), getOutputFinal(), getOutputTimestamp());
    }
}