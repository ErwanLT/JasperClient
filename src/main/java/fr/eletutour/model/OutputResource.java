package fr.eletutour.model;

public class OutputResource {
    private String contentType;
    private String fileName;
    private Boolean outputFinal;
    private Long outputTimestamp;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getOutputFinal() {
        return outputFinal;
    }

    public void setOutputFinal(Boolean outputFinal) {
        this.outputFinal = outputFinal;
    }

    public Long getOutputTimestamp() {
        return outputTimestamp;
    }

    public void setOutputTimestamp(Long outputTimestamp) {
        this.outputTimestamp = outputTimestamp;
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
}
