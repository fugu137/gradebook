package gradebook.model;

import gradebook.enums.AssessmentType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StdAssessment implements Assessment {

    private StringProperty name;
    private AssessmentType type;
    private DoubleProperty weighting;

    public StdAssessment(String name, AssessmentType type, Double weighting) {
        this.name = new SimpleStringProperty(name);
        this.type = type;

        if (weighting != null) {
            this.weighting = new SimpleDoubleProperty(weighting);
        } else {
            this.weighting = null;
        }

    }

    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Double getWeighting() {
        return weighting.getValue();
    }

    public void setWeighting(Double weighting) {
        this.weighting.set(weighting);
    }

    public AssessmentType getType() {
        return type;
    }

    @Override
    public void setType(AssessmentType type) {
        this.type = type;
    }

}
