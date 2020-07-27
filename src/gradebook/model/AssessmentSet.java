package gradebook.model;

import gradebook.enums.AssessmentType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssessmentSet implements Assessment {

    private StringProperty name;
    private ObservableList<StdAssessment> stdAssessments;
    private AssessmentType type;
    private DoubleProperty weighting;
    private IntegerProperty quantity;
    private IntegerProperty bestOf;

    public AssessmentSet(String name, AssessmentType type, Double weighting, Integer quantity, Integer bestOf) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.weighting = new SimpleDoubleProperty(weighting);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.bestOf = new SimpleIntegerProperty(bestOf);

        createAssessments();
    }

    private void createAssessments() {
        this.stdAssessments = FXCollections.observableArrayList();

        for (int i = 0; i < quantity.getValue(); i++) {
            stdAssessments.add(new StdAssessment(name.getValue() + " " + (i + 1), type, null));
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

    public Integer getQuantity() {
        return quantity.getValue();
    }

    public Integer getBestOf() {
        return bestOf.getValue();
    }

    public AssessmentType getType() {
        return type;
    }

}
