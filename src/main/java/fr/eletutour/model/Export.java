package fr.eletutour.model;

public class Export {
    private String status;
    private String id;
    private OutputResource resource;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OutputResource getResource() {
        return resource;
    }

    public void setResource(OutputResource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Export{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", resource=" + resource +
                '}';
    }
}
