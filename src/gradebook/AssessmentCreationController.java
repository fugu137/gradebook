package gradebook;

import gradebook.enums.AssessmentForm;
import gradebook.enums.AssessmentType;
import gradebook.model.AssessmentCreationForm;
import gradebook.model.AssessmentCreator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AssessmentCreationController implements Initializable {
    @FXML
    private ComboBox<AssessmentType> comboBox1;
    @FXML
    private ComboBox<AssessmentForm> modalityBox1;
    @FXML
    private Button clearButton1;
    @FXML
    private TextField nameField1;
    @FXML
    private TextField quantityField1;
    @FXML
    private TextField bestOfField1;
    @FXML
    private TextField weightField1;

    @FXML
    private ComboBox<AssessmentType> comboBox2;
    @FXML
    private ComboBox<AssessmentForm> modalityBox2;
    @FXML
    private Button clearButton2;
    @FXML
    private TextField nameField2;
    @FXML
    private TextField quantityField2;
    @FXML
    private TextField bestOfField2;
    @FXML
    private TextField weightField2;

    @FXML
    private ComboBox<AssessmentType> comboBox3;
    @FXML
    private ComboBox<AssessmentForm> modalityBox3;
    @FXML
    private Button clearButton3;
    @FXML
    private TextField nameField3;
    @FXML
    private TextField quantityField3;
    @FXML
    private TextField bestOfField3;
    @FXML
    private TextField weightField3;

    @FXML
    private ComboBox<AssessmentType> comboBox4;
    @FXML
    private ComboBox<AssessmentForm> modalityBox4;
    @FXML
    private Button clearButton4;
    @FXML
    private TextField nameField4;
    @FXML
    private TextField quantityField4;
    @FXML
    private TextField bestOfField4;
    @FXML
    private TextField weightField4;

    @FXML
    private ComboBox<AssessmentType> comboBox5;
    @FXML
    private ComboBox<AssessmentForm> modalityBox5;
    @FXML
    private Button clearButton5;
    @FXML
    private TextField nameField5;
    @FXML
    private TextField quantityField5;
    @FXML
    private TextField bestOfField5;
    @FXML
    private TextField weightField5;

    @FXML
    private ComboBox<AssessmentType> comboBox6;
    @FXML
    private ComboBox<AssessmentForm> modalityBox6;
    @FXML
    private Button clearButton6;
    @FXML
    private TextField nameField6;
    @FXML
    private TextField quantityField6;
    @FXML
    private TextField bestOfField6;
    @FXML
    private TextField weightField6;

    @FXML
    private ComboBox<AssessmentType> comboBox7;
    @FXML
    private ComboBox<AssessmentForm> modalityBox7;
    @FXML
    private Button clearButton7;
    @FXML
    private TextField nameField7;
    @FXML
    private TextField quantityField7;
    @FXML
    private TextField bestOfField7;
    @FXML
    private TextField weightField7;

    @FXML
    private ComboBox<AssessmentType> comboBox8;
    @FXML
    private ComboBox<AssessmentForm> modalityBox8;
    @FXML
    private Button clearButton8;
    @FXML
    private TextField nameField8;
    @FXML
    private TextField quantityField8;
    @FXML
    private TextField bestOfField8;
    @FXML
    private TextField weightField8;

    @FXML
    private TextField totalWeightField;

    @FXML
    private Button finaliseButton;
    @FXML
    private Button cancelButton;

    //Control//
    private MainController mainController;
    private AssessmentCreator assessmentCreator;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBoxes();
        setupAssessmentCreator();
    }

    //Initialize Methods//
    private void fillComboBoxes() {
        List<AssessmentType> assessmentList = Arrays.asList(AssessmentType.values());
        comboBox1.getItems().addAll(assessmentList);
        comboBox2.getItems().addAll(assessmentList);
        comboBox3.getItems().addAll(assessmentList);
        comboBox4.getItems().addAll(assessmentList);
        comboBox5.getItems().addAll(assessmentList);
        comboBox6.getItems().addAll(assessmentList);
        comboBox7.getItems().addAll(assessmentList);
        comboBox8.getItems().addAll(assessmentList);

        List<AssessmentForm> assessmentFormList = Arrays.asList(AssessmentForm.values());
        modalityBox1.getItems().addAll(assessmentFormList);
        modalityBox2.getItems().addAll(assessmentFormList);
        modalityBox3.getItems().addAll(assessmentFormList);
        modalityBox4.getItems().addAll(assessmentFormList);
        modalityBox5.getItems().addAll(assessmentFormList);
        modalityBox6.getItems().addAll(assessmentFormList);
        modalityBox7.getItems().addAll(assessmentFormList);
        modalityBox8.getItems().addAll(assessmentFormList);
    }

    private void setupAssessmentCreator() {
        assessmentCreator = new AssessmentCreator();

        AssessmentCreationForm form1 = new AssessmentCreationForm(modalityBox1, comboBox1, nameField1, quantityField1, bestOfField1, weightField1);
        assessmentCreator.addForm(form1);
        AssessmentCreationForm form2 = new AssessmentCreationForm(modalityBox2, comboBox2, nameField2, quantityField2, bestOfField2, weightField2);
        assessmentCreator.addForm(form2);
        AssessmentCreationForm form3 = new AssessmentCreationForm(modalityBox3, comboBox3, nameField3, quantityField3, bestOfField3, weightField3);
        assessmentCreator.addForm(form3);
        AssessmentCreationForm form4 = new AssessmentCreationForm(modalityBox4, comboBox4, nameField4, quantityField4, bestOfField4, weightField4);
        assessmentCreator.addForm(form4);
        AssessmentCreationForm form5 = new AssessmentCreationForm(modalityBox5, comboBox5, nameField5, quantityField5, bestOfField5, weightField5);
        assessmentCreator.addForm(form5);
        AssessmentCreationForm form6 = new AssessmentCreationForm(modalityBox6, comboBox6, nameField6, quantityField6, bestOfField6, weightField6);
        assessmentCreator.addForm(form6);
        AssessmentCreationForm form7 = new AssessmentCreationForm(modalityBox7, comboBox7, nameField7, quantityField7, bestOfField7, weightField7);
        assessmentCreator.addForm(form7);
        AssessmentCreationForm form8 = new AssessmentCreationForm(modalityBox8, comboBox8, nameField8, quantityField8, bestOfField8, weightField8);
        assessmentCreator.addForm(form8);
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


}
