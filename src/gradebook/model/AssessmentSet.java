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

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableList<StdAssessment> getStdAssessments() {
        return stdAssessments;
    }

    public Double getWeighting() {
        return weighting.getValue();
    }

    public void setWeighting(Double weighting) {
        this.weighting.set(weighting);
    }

    public Integer getQuantity() {
        return quantity.getValue();
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    public Integer getBestOf() {
        return bestOf.getValue();
    }

    public void setBestOf(Integer bestOf) {
        this.bestOf.set(bestOf);
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public void renameStdAssessments(String[] names) {
        for (int i = 0; i < names.length; i++) {
            stdAssessments.get(i).setName(names[i]);
        }
    }

}
