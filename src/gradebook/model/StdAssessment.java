package gradebook.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StdAssessment implements Assessment {

    private StringProperty name;
    private AssessmentType type;
    private DoubleProperty weighting;

    public StdAssessment(String name, AssessmentType type, double weighting) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.weighting = new SimpleDoubleProperty(weighting);
    }

    public String getName() {
        return name.getValue();
    }

    public Double getWeighting() {
        return weighting.getValue();
    }

}
