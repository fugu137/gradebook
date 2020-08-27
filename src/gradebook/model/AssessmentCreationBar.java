package gradebook.model;

import gradebook.enums.AssessmentForm;
import gradebook.enums.AssessmentType;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<String> errorMessages;

    public AssessmentCreationBar(ComboBox<AssessmentForm> formComboBox, ComboBox<AssessmentType> typeComboBox, Button clearButton, TextField nameField, TextField quantityField, TextField bestOfField, TextField weightingField) {
        this.formComboBox = formComboBox;
        this.typeComboBox = typeComboBox;
        this.clearButton = clearButton;
        this.nameField = nameField;
        this.quantityField = quantityField;
        this.bestOfField = bestOfField;
        this.weightingField = weightingField;

        this.isActive = new SimpleBooleanProperty(false);
        this.errorMessages = FXCollections.observableArrayList();

        initialSettings();
    }

    private void initialSettings() {
        clearButton.disableProperty().bind(formComboBox.disableProperty().not());
        clearButton.setUserData(this);
        BooleanBinding notSetAssessment = formComboBox.getSelectionModel().selectedItemProperty().isNotEqualTo(AssessmentForm.SET);

        quantityField.disableProperty().bind(notSetAssessment);
        bestOfField.disableProperty().bind(notSetAssessment);

        BooleanBinding noFormSelected = formComboBox.getSelectionModel().selectedItemProperty().isNull();
        typeComboBox.disableProperty().bind(noFormSelected);
        nameField.disableProperty().bind(noFormSelected);
        weightingField.disableProperty().bind(noFormSelected);

        isActive.bind(noFormSelected.not());

        setDefaultFieldStyles();
    }

    private void setDefaultFieldStyles() {
        typeComboBox.setOnAction(e -> {
            if (typeComboBox.getValue() != null) {
                typeComboBox.getStyleClass().remove("error-combo-box");
                typeComboBox.getStyleClass().add("toolbar-combo-box");
            }
        });

        nameField.setOnKeyTyped(e -> {
            nameField.getStyleClass().remove("error-name-field");
            nameField.getStyleClass().add("name-field");
        });

        quantityField.setOnKeyTyped(e -> {
            quantityField.getStyleClass().remove("error-number-field");
            quantityField.getStyleClass().add("number-field");
        });

        bestOfField.setOnKeyTyped(e -> {
            bestOfField.getStyleClass().remove("error-number-field");
            bestOfField.getStyleClass().add("number-field");
        });

        weightingField.setOnKeyTyped(e -> {
            weightingField.getStyleClass().clear();
            weightingField.getStyleClass().add("number-field");
        });
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

    public TextField getWeightingField() {
        return weightingField;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public boolean isActive() {
        return isActive.getValue();
    }

    public boolean hasInvalidEntries() {

        errorMessages.clear();

        boolean invalid = false;

        if (isActive.getValue()) {

            if (typeComboBox.getValue() == null) {
                typeComboBox.getStyleClass().add("error-combo-box");
                this.errorMessages.add("- Assessment types cannot be blank.");
                invalid = true;
            }

            if (nameField.getText().isBlank()) {
                nameField.getStyleClass().add("error-name-field");
                this.errorMessages.add("- Assessment names cannot be blank.");
                invalid = true;
            }

            if (!quantityField.isDisabled()) {
                if (!quantityField.getText().matches("\\d|\\d\\d")) {
                    quantityField.getStyleClass().add("error-number-field");
                    this.errorMessages.add("- Quantities must be numbers between 0 and 99.");
                    invalid = true;
                }
            }

            if (!bestOfField.isDisabled()) {
                if (!bestOfField.getText().matches("\\d|\\d\\d")) {
                    bestOfField.getStyleClass().add("error-number-field");
                    this.errorMessages.add("- Best of values must be numbers between 0 and 99.");
                    invalid = true;
                }

                if (bestOfField.getText().matches("\\d|\\d\\d") && !(Integer.parseInt(bestOfField.getText()) <= Integer.parseInt(quantityField.getText()))) {
                    bestOfField.getStyleClass().add("error-number-field");
                    this.errorMessages.add("- Best-of values must be less than or equal to the corresponding number of assessments.");
                    invalid = true;
                }
            }

            if (weightingField.getText() != null) {
                if (!weightingField.getText().matches("100|\\d\\d|\\d")) {
                    weightingField.getStyleClass().add("error-number-field");
                    this.errorMessages.add("- Weightings must be numbers between 0 and 100.");
                    invalid = true;
                }
            }

        }
        return invalid;
    }

    public Assessment clear() {
        Assessment toReturn = this.assessment;
        this.assessment = null;

        formComboBox.setDisable(false);
        formComboBox.getSelectionModel().clearSelection();
        typeComboBox.getSelectionModel().clearSelection();
        nameField.clear();
        quantityField.clear();
        bestOfField.clear();
        weightingField.clear();

        return toReturn;
    }

    public ObservableList<String> getErrorMessages() {
        return errorMessages;
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
            formComboBox.setDisable(true);
        }
    }

    public void modifyAssessment() {

        if (isActive.getValue() && !this.hasInvalidEntries()) {
            AssessmentForm assessmentForm = getForm();
            AssessmentType type = getType();
            String name = getName();
            Integer quantity = getQuantity();
            Integer bestOf = getBestOf();
            Double weighting = (double) ((int) getWeighting()) / 100;

            if (assessment instanceof StdAssessment) {
                StdAssessment stdAssessment = (StdAssessment) assessment;
                stdAssessment.setType(type);
                stdAssessment.setName(name);
                stdAssessment.setWeighting(weighting);
            }

            if (assessment instanceof AssessmentSet) {
                AssessmentSet set = (AssessmentSet) assessment;
                set.setType(type);
                set.setName(name);
                set.setWeighting(weighting);
                set.setQuantity(quantity);
                set.setBestOf(bestOf);
            }
        }
    }


}

