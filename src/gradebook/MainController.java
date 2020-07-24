package gradebook;

import gradebook.enums.AssessmentType;
import gradebook.enums.Gender;
import gradebook.model.Class;
import gradebook.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    //Table//
    @FXML
    private TableView<Student> table;

    @FXML
    private TableColumn<Student, String> surnameColumn;
    @FXML
    private TableColumn<Student, String> givenNamesColumn;
    @FXML
    private TableColumn<Student, String> preferredNameColumn;
    @FXML
    private TableColumn<Student, Class> classColumn;
    @FXML
    private TableColumn<Student, Gender> genderColumn;
    @FXML
    private TableColumn<Student, Integer> sidColumn;
    @FXML
    private TableColumn<Student, String> degreeColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;

    //Toolbar//
    @FXML
    private MenuItem loadMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;

    @FXML
    private SplitMenuButton importButton;

    @FXML
    private Button copyButton;
    @FXML
    private Button cutButton;
    @FXML
    private Button pasteButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button selectAllButton;
    @FXML
    private Button selectNoneButton;
    @FXML
    private Button deleteButton;

    @FXML
    private CheckBox essaysCheckBox;
    @FXML
    private CheckBox essayPlansCheckBox;
    @FXML
    private CheckBox examsCheckBox;
    @FXML
    private CheckBox quizzesCheckBox;
    @FXML
    private CheckBox argAnalysesCheckBox;
    @FXML
    private CheckBox participationCheckBox;
    @FXML
    private CheckBox presentationsCheckBox;
    @FXML
    private CheckBox otherCheckBox;

    @FXML
    private ComboBox<Class> classListBox;

    @FXML
    private ToggleButton studentViewToggleButton;
    @FXML
    private ToggleButton anonymiseToggleButton;

    @FXML
    private Button addAssessmentsButton;


    //Control//
    private CourseManager courseManager = new CourseManager("PHIL1011");    //TODO: request course name
    private Student blankStudent;

    private Scene assessmentCreationWindow;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newBlankStudent();
        addDummyData();

        table.getItems().addAll(courseManager.getAllStudents());

        loadTableSettings();
        setupColumns();
        populateColumns();
    }

    //Initialize Methods//
    private void newBlankStudent() {
        blankStudent = new Student();
        table.getItems().add(blankStudent);

        addBlankStudentListeners();
    }

    private void addBlankStudentListeners() {

        blankStudent.surnameProperty().addListener(obs -> {
            if (blankStudent.surnameProperty() != null && !blankStudent.getSurname().isBlank()) {
                courseManager.newStudent(blankStudent);
                newBlankStudent();
            }
        });

        blankStudent.givenNamesProperty().addListener(obs -> {
            if (blankStudent.givenNamesProperty() != null && !blankStudent.getGivenNames().isBlank()) {
                courseManager.newStudent(blankStudent);
                newBlankStudent();
            }
        });
    }

    private void loadTableSettings() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        table.sortPolicyProperty().set(t -> {
            Comparator<Student> comparator = (r1, r2) ->
                    r1 == blankStudent ? 1
                            : r2 == blankStudent ? -1
                            : t.getComparator() == null ? 0
                            : t.getComparator().compare(r1, r2);
            FXCollections.sort(t.getItems(), comparator);
            return true;
        });
    }

    private void setupColumns() {
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        givenNamesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        preferredNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sidColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        degreeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        classColumn.setCellFactory(c -> {
            ComboBoxTableCell<Student, Class> comboBoxCell = new ComboBoxTableCell<>(courseManager.getClasses());
            comboBoxCell.setComboBoxEditable(true);

            comboBoxCell.setConverter(new StringConverter<>() {
                @Override
                public String toString(Class classGroup) {
                    return classGroup == null ? "" : classGroup.getName();
                }

                @Override
                public Class fromString(String className) {
                    Class classGroup = courseManager.getClass(className);

                    if (classGroup != null) {
                        return classGroup;
                    } else {
                        Class newClass = new Class(className);
                        courseManager.addClass(newClass);
                        return newClass;
                    }
                }
            });

            return comboBoxCell;
        });

        genderColumn.setCellFactory(c -> new ComboBoxTableCell<>(Gender.values()));

//        preferredNameColumn.setVisible(false);
//        sidColumn.setVisible(false);
//        degreeColumn.setVisible(false);
//        emailColumn.setVisible(false);
//        genderColumn.setVisible(false);
    }

    private void populateColumns() {
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        givenNamesColumn.setCellValueFactory(new PropertyValueFactory<>("givenNames"));
        preferredNameColumn.setCellValueFactory(new PropertyValueFactory<>("preferredName"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("classGroup"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        sidColumn.setCellValueFactory(new PropertyValueFactory<>("sid"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<>("degree"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    //Table Methods//
    @FXML
    public void editClassCell(TableColumn.CellEditEvent<Student, Class> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        Class classGroup = editedCell.getNewValue();
        selectedStudent.setClassGroup(classGroup);
    }

    @FXML
    public void editGenderCell(TableColumn.CellEditEvent<Student, Gender> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        Gender gender = editedCell.getNewValue();
        selectedStudent.setGender(gender);
    }

    //Toolbar Methods//

    public void displayAssessmentCreationWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assessment-creation-window.fxml"));
        Parent root = loader.load();

        AssessmentCreationController assessmentSetupController = loader.getController();
        assessmentSetupController.setMainController(this);

        assessmentCreationWindow = new Scene(root);

        stage = new Stage();
        stage.setScene(assessmentCreationWindow);
        stage.setTitle("Create Assessments");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.resizableProperty().setValue(false);
        stage.showAndWait();
    }


    //Test Methods//
    public void addDummyData() {
        Class w15A = new Class("W15A");
        Class t17B = new Class("T17B");
//        courseManager.addClass(w15A);
//        courseManager.addClass(t17B);

        Class r11C = new Class("R11C");

        Student fred = new Student("Fredson", "Fred", "Freddie", w15A, Gender.M, 1035, "DH205", "fred@mail.com");
        Student mary = new Student("McKillop", "Mary", "Mar", t17B, Gender.F, 2022, "LH304", "mary@mail.com");
        Student jane = new Student("Dawson", "Jane", "J", null, Gender.F, 1090, "DH107", "jane@mail.com");

        courseManager.newStudent(fred);
        courseManager.newStudent(mary);
        courseManager.newStudent(jane);

        AssignedAssessments assignedAssessments = new AssignedAssessments("Assigned Assessments");
        StdAssessment essay = new StdAssessment("Essay", AssessmentType.ESSAY, 0.4);
        StdAssessment exam = new StdAssessment("Exam", AssessmentType.EXAM, 0.4);
        AssessmentSet quiz = new AssessmentSet("Quiz", AssessmentType.QUIZ, 0.2, 5, 4);
//        assignedAssessments.addAssessment(essay);
//        assignedAssessments.addAssessment(exam);
//        assignedAssessments.addAssessmentSet(quiz);

//        fred.addStdAssessmentData(essay);
//        fred.addStdAssessmentData(exam);
        fred.addAssessmentSetData(quiz);

//        mary.addStdAssessmentData(essay);
//        mary.addStdAssessmentData(exam);
        mary.addAssessmentSetData(quiz);

//        jane.addStdAssessmentData(essay);
//        jane.addStdAssessmentData(exam);
        jane.addAssessmentSetData(quiz);

//        blankStudent.addStdAssessmentData(essay);
//        blankStudent.addStdAssessmentData(exam);
        blankStudent.addAssessmentSetData(quiz);

//        AssessmentColumn<Student, Integer> essayColumn = new AssessmentColumn<>(essay.getName(), essay);
//        essayColumn.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(essay));
//        essayColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//        table.getColumns().add(essayColumn);
//
//        AssessmentColumn<Student, Integer> examColumn = new AssessmentColumn<>(exam.getName(), exam);
//        examColumn.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(exam));
//        examColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//        table.getColumns().add(examColumn);

        for (StdAssessment q: quiz.getStdAssessments()) {
            AssessmentColumn<Student, Integer> quizColumn = new AssessmentColumn<>(q.getName(), q);
            quizColumn.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(quiz, q));
            quizColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            table.getColumns().add(quizColumn);
        }

        AssessmentColumn<Student, Double> quizTotalColumn = new AssessmentColumn<>(quiz.getName() + " Total", quiz);
        quizTotalColumn.setCellValueFactory(c -> c.getValue().assessmentSetTotalGradeProperty(quiz));
        table.getColumns().add(quizTotalColumn);

        AssessmentColumn<Student, Double> totalColumn = new AssessmentColumn<>("Total Mark");
        totalColumn.setCellValueFactory(c -> c.getValue().totalGradeProperty());
        table.getColumns().add(totalColumn);

    }


    @FXML
    public void printSelectedStudent() {
        System.out.println(table.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void printStudents() {
        System.out.println("Enrolled Students");
        courseManager.getAllStudents().forEach(System.out::println);
        System.out.println();

        System.out.println("Table Students");
        ObservableList<Student> students = table.getItems();
        students.forEach(System.out::println);
    }
}


