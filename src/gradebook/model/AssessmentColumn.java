package gradebook.model;

import javafx.scene.control.TableColumn;

public class AssessmentColumn<Student, Integer> extends TableColumn<Student, Integer> {

    private StdAssessment stdAssessment;

    public AssessmentColumn(String name, StdAssessment stdAssessment) {
        super(name);
        this.stdAssessment = stdAssessment;
    }

}


