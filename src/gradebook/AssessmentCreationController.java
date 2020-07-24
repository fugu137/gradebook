package gradebook;

import gradebook.enums.AssessmentForm;
import gradebook.enums.AssessmentType;
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

    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBoxes();
        setupAssessmentCreators();
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

    private void setupAssessmentCreators() {
//        AssessmentCreator assessmentCreator1 = new AssessmentCreator();
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }



}
