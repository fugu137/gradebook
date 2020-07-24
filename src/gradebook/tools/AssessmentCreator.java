package gradebook.tools;

import gradebook.enums.AssessmentForm;
import gradebook.enums.AssessmentType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AssessmentCreator {

    private ComboBox<AssessmentForm> formComboBox;
    private ComboBox<AssessmentType> typeComboBox;
    private TextField nameField;
    private TextField quantityField;
    private TextField bestOfField;
    private TextField weightingField;

    public AssessmentCreator(ComboBox<AssessmentForm> formComboBox, ComboBox<AssessmentType> typeComboBox, TextField nameField, TextField quantityField, TextField bestOfField, TextField weightingField) {
        this.formComboBox = formComboBox;
        this.typeComboBox = typeComboBox;
        this.nameField = nameField;
        this.quantityField = quantityField;
        this.bestOfField = bestOfField;
        this.weightingField = weightingField;
    }
}
