package fr.eletutour.model;

public enum OutputFormat {
    PDF("PDF"),
    HTML("HTML");

    private String format;

    OutputFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
