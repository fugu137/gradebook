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

    public Double getWeighting() {
        return weighting.getValue();
    }

    public AssessmentType getType() {
        return type;
    }

}
