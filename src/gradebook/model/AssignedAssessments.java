package gradebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssignedAssessments {

    private StringProperty name;
    private ObservableList<StdAssessment> stdAssessments;
    private ObservableList<AssessmentSet> assessmentSets;

    public AssignedAssessments(String name) {
        this.name = new SimpleStringProperty(name);
        this.stdAssessments = FXCollections.observableArrayList();
        this.assessmentSets = FXCollections.observableArrayList();
    }

    public ObservableList<StdAssessment> getStdAssessments() {
        return stdAssessments;
    }

    public ObservableList<AssessmentSet> getAssessmentSets() {
        return assessmentSets;
    }

    public void addAssessment(StdAssessment stdAssessment) {

        if (!stdAssessments.contains(stdAssessment)) {
            stdAssessments.add(stdAssessment);
        } else {
            System.out.println("Assessment already exists! [AssignedAssessments > addAssessment]");
        }
    }

    public void addAssessmentSet(AssessmentSet assessmentSet) {
        if (!assessmentSets.contains(assessmentSet)) {
            assessmentSets.add(assessmentSet);
        } else {
            System.out.println("AssessmentSet already exists! [AssignedAssessments > addAssessmentSet]");
        }
    }

}
