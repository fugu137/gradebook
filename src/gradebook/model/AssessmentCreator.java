package gradebook.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssessmentCreator {

    ObservableList<AssessmentCreationForm> forms;

    public AssessmentCreator() {
        this.forms = FXCollections.observableArrayList();
    }

    public void addForm(AssessmentCreationForm form) {
        this.forms.add(form);
    }

}
