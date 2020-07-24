package gradebook.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssessmentSet implements Assessment {

    private StringProperty name;
    private ObservableList<StdAssessment> stdAssessments;
    private DoubleProperty weighting;
    private IntegerProperty bestOf;

    public AssessmentSet(String name, AssessmentType type, double weighting, int quantity, int bestOf) {
        this.name = new SimpleStringProperty(name);
        this.weighting = new SimpleDoubleProperty(weighting);
        this.bestOf = new SimpleIntegerProperty(bestOf);

        createAssessments(type, quantity);
    }

    private void createAssessments(AssessmentType type, int quantity) {
        this.stdAssessments = FXCollections.observableArrayList();

        for (int i = 0; i < quantity; i++) {
            stdAssessments.add(new StdAssessment(name.getValue() + " " + (i + 1), type, 1.0/quantity));
        }
    }

    public String getName() {
        return name.getValue();
    }

    public ObservableList<StdAssessment> getStdAssessments() {
        return stdAssessments;
    }

    public Double getWeighting() {
        return weighting.getValue();
    }

}
