package fr.eletutour.model.request.execution;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Objects;

public class ReportParameter {
    @XmlAttribute
    private final String name = "jsonString";

    private String value;

    public ReportParameter(){}

    public ReportParameter value(String value){
        this.setValue(value);
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportParameter that)) return false;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getValue());
    }
}
