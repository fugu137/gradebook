package gradebook.model;

import gradebook.enums.AssessmentForm;
import gradebook.enums.AssessmentType;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AssessmentCreationBar {

    private ComboBox<AssessmentForm> formComboBox;
    private ComboBox<AssessmentType> typeComboBox;
    private Button clearButton;
    private TextField nameField;
    private TextField quantityField;
    private TextField bestOfField;
    private TextField weightingField;

    private BooleanProperty isActive;
    private Assessment assessment;

    public AssessmentCreationBar(ComboBox<AssessmentForm> formComboBox, ComboBox<AssessmentType> typeComboBox, Button clearButton, TextField nameField, TextField quantityField, TextField bestOfField, TextField weightingField) {
        this.formComboBox = formComboBox;
        this.typeComboBox = typeComboBox;
        this.clearButton = clearButton;
        this.nameField = nameField;
        this.quantityField = quantityField;
        this.bestOfField = bestOfField;
        this.weightingField = weightingField;

        this.isActive = new SimpleBooleanProperty(false);

        initialSettings();
    }

    private void initialSettings() {
        clearButton.disableProperty().bind(formComboBox.disableProperty().not());
        BooleanBinding notSetAssessment = formComboBox.getSelectionModel().selectedItemProperty().isNotEqualTo(AssessmentForm.SET);

        quantityField.disableProperty().bind(notSetAssessment);
        bestOfField.disableProperty().bind(notSetAssessment);

        BooleanBinding noFormSelected = formComboBox.getSelectionModel().selectedItemProperty().isNull();
        typeComboBox.disableProperty().bind(noFormSelected);
        nameField.disableProperty().bind(noFormSelected);
        weightingField.disableProperty().bind(noFormSelected);

        isActive.bind(noFormSelected.not());
    }

    public ComboBox<AssessmentForm> getFormComboBox() {
        return formComboBox;
    }

    public AssessmentForm getForm() {
        return formComboBox.getValue();
    }

    public AssessmentType getType() {
        return typeComboBox.getValue();
    }

    public String getName() {
        return nameField.getText();
    }

    public Integer getQuantity() {
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Integer getBestOf() {
        try {
            return Integer.parseInt(bestOfField.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Integer getWeighting() {
        try {
            return Integer.parseInt(weightingField.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public boolean isActive() {
        return isActive.getValue();
    }

    public boolean hasInvalidEntries() {

        return false; //TODO: complete
//        if (!quantityField.getText().matches("\\d|\\d\\d") && !bestOfField.getText().matches("\\d|\\d\\d") && !weightingField.getText().matches("100|\\d\\d|\\d")) {
//            return true;
//
//        } else if (Integer.parseInt(bestOfField.getText()) > Integer.parseInt(quantityField.getText())) {
//            return true;
//
//        } else {
//            return false;
//        }
    }

    public void createAssessment() {

        if (isActive.getValue() && !this.hasInvalidEntries()) {

            AssessmentForm assessmentForm = getForm();
            AssessmentType type = getType();
            String name = getName();
            Integer quantity = getQuantity();
            Integer bestOf = getBestOf();
            Double weighting = (double) ((int) getWeighting()) / 100;

            switch (assessmentForm) {
                case SINGLE:
                    assessment = new StdAssessment(name, type, weighting);
                    break;
                case SET:
                    assessment = new AssessmentSet(name, type, weighting, quantity, bestOf);
                    break;
                default:
                    throw new IllegalArgumentException("Not a valid assessment form!");
            }
        }
    }
}
