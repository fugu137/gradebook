package gradebook;

import gradebook.enums.AssessmentType;
import gradebook.enums.Gender;
import gradebook.model.Class;
import gradebook.model.*;
import gradebook.tools.FileChooserWindow;
import gradebook.tools.StudentImporter;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Window;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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


    private ObservableList<AssessmentColumn<Student, ?>> essayColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> examColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> essayPlanColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> participationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> argAnalysisColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> quizColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> presentationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> otherColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});

    //Control//
    private CourseManager courseManager = new CourseManager("PHIL1011");    //TODO: request course name
    private Student blankStudent;

    private Scene assessmentCreationWindow;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.setItems(courseManager.getAllStudents());

//        addDummyData();
        newBlankStudent();

        loadTableSettings();
        setupColumns();
        populateColumns();

        loadToolbarSettings();
        setupToolbarBindings();
    }

    //Initialize Methods//
    private void newBlankStudent() {
        blankStudent = new Student();
        table.getItems().add(blankStudent);
//
//        addBlankStudentListeners();
    }
//
//    private void addBlankStudentListeners() {
//
//        blankStudent.surnameProperty().addListener(obs -> {
//            if (!blankStudent.getSurname().isBlank()) {
//                courseManager.newStudent(blankStudent);
//                newBlankStudent();
//            }
//        });
//
//        blankStudent.givenNamesProperty().addListener(obs -> {
//            if (!blankStudent.getGivenNames().isBlank()) {
//                courseManager.newStudent(blankStudent);
//                newBlankStudent();
//            }
//        });
//    }

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

    private void loadToolbarSettings() {
        studentViewToggleButton.setSelected(true);

        saveMenuItem.setDisable(true);

        essaysCheckBox.setDisable(true);
        examsCheckBox.setDisable(true);
        essayPlansCheckBox.setDisable(true);
        argAnalysesCheckBox.setDisable(true);
        participationCheckBox.setDisable(true);
        quizzesCheckBox.setDisable(true);
        presentationsCheckBox.setDisable(true);
        otherCheckBox.setDisable(true);
    }

    private void setupToolbarBindings() {
        toggleButtonBindings();
        checkBoxBindings();
    }

    private void toggleButtonBindings() {

        studentViewToggleButton.selectedProperty().addListener(obs -> {
            ObservableList<TableColumn<Student, ?>> columns = table.getColumns();

            if (studentViewToggleButton.isSelected()) {
                setStudentInfoView(true);

            } else {
                setStudentInfoView(false);
            }
        });

        anonymiseToggleButton.selectedProperty().addListener(obs -> {

            if (anonymiseToggleButton.isSelected()) {
                studentViewToggleButton.setSelected(false);

                surnameColumn.setVisible(false);
                givenNamesColumn.setVisible(false);

            } else {
                studentViewToggleButton.setSelected(true);
            }
        });
    }

    private void setStudentInfoView(boolean on) {
        ObservableList<TableColumn<Student, ?>> columns = table.getColumns();

        if (on) {
            anonymiseToggleButton.setSelected(false);

            for (TableColumn<Student, ?> c : columns) {
                if (!(c instanceof AssessmentColumn)) {
                    c.setVisible(true);
                };
            }

        } else {

            for (TableColumn<Student, ?> c : columns) {
                c.setVisible(c instanceof AssessmentColumn);
            }

            surnameColumn.setVisible(true);
            givenNamesColumn.setVisible(true);
            classColumn.setVisible(true);
            preferredNameColumn.setVisible(false);
            genderColumn.setVisible(false);
            sidColumn.setVisible(false);
            degreeColumn.setVisible(false);
            emailColumn.setVisible(false);
        }
    }

    private void checkBoxBindings() {
        essaysCheckBoxBinding();
        essayColumnBinding();

        examsCheckBoxBinding();
        examColumnsBinding();

        essayPlansCheckBoxBinding();
        essayPlanColumnBinding();

        argAnalysesCheckBoxBinding();
        argAnalysisColumnBinding();

        participationCheckBoxBinding();
        participationColumnBinding();

        quizzesCheckBoxBinding();
        quizColumnBinding();

        presentationsCheckBoxBinding();
        presentationColumnBinding();

        otherCheckBoxBinding();
        otherColumnBinding();
    }

    private void essaysCheckBoxBinding() {

        essaysCheckBox.selectedProperty().addListener(obs -> {
            if ((essaysCheckBox.isSelected())) {
                essayColumns.forEach(c -> c.setVisible(true));
            } else {
                essayColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void essayColumnBinding() {

        essayColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                essaysCheckBox.setDisable(essayColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : essayColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }

                essaysCheckBox.setSelected(allVisible);
            }
        });
    }

    private void examsCheckBoxBinding() {

        examsCheckBox.selectedProperty().addListener(obs -> {
            if ((examsCheckBox.isSelected())) {
                examColumns.forEach(c -> c.setVisible(true));
            } else {
                examColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void examColumnsBinding() {

        examColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                examsCheckBox.setDisable(examColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : examColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }

                examsCheckBox.setSelected(allVisible);
            }
        });
    }

    private void essayPlansCheckBoxBinding() {

        essayPlansCheckBox.selectedProperty().addListener(obs -> {
            if ((essayPlansCheckBox.isSelected())) {
                essayColumns.forEach(c -> c.setVisible(true));
            } else {
                essayPlanColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void essayPlanColumnBinding() {

        essayPlanColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                essayPlansCheckBox.setDisable(essayPlanColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : essayPlanColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }

                essayPlansCheckBox.setSelected(allVisible);
            }
        });
    }

    private void argAnalysesCheckBoxBinding() {

        argAnalysesCheckBox.selectedProperty().addListener(obs -> {
            if ((argAnalysesCheckBox.isSelected())) {
                argAnalysisColumns.forEach(c -> c.setVisible(true));
            } else {
                argAnalysisColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void argAnalysisColumnBinding() {

        argAnalysisColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                argAnalysesCheckBox.setDisable(argAnalysisColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : argAnalysisColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }

                argAnalysesCheckBox.setSelected(allVisible);
            }
        });
    }

    private void participationCheckBoxBinding() {

        participationCheckBox.selectedProperty().addListener(obs -> {
            if ((participationCheckBox.isSelected())) {
                participationColumns.forEach(c -> c.setVisible(true));
            } else {
                participationColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void participationColumnBinding() {

        participationColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                participationCheckBox.setDisable(participationColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : participationColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }
                participationCheckBox.setSelected(allVisible);
            }
        });
    }

    private void quizzesCheckBoxBinding() {

        quizzesCheckBox.selectedProperty().addListener(obs -> {
            if ((quizzesCheckBox.isSelected())) {
                quizColumns.forEach(c -> c.setVisible(true));
            } else {
                quizColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void quizColumnBinding() {

        quizColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                quizzesCheckBox.setDisable(quizColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : quizColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }
                quizzesCheckBox.setSelected(allVisible);
            }
        });
    }

    private void presentationsCheckBoxBinding() {

        presentationsCheckBox.selectedProperty().addListener(obs -> {
            if ((presentationsCheckBox.isSelected())) {
                presentationColumns.forEach(c -> c.setVisible(true));
            } else {
                presentationColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void presentationColumnBinding() {

        presentationColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                presentationsCheckBox.setDisable(presentationColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : presentationColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }
                presentationsCheckBox.setSelected(allVisible);
            }
        });
    }

    private void otherCheckBoxBinding() {

        otherCheckBox.selectedProperty().addListener(obs -> {
            if ((otherCheckBox.isSelected())) {
                otherColumns.forEach(c -> c.setVisible(true));
            } else {
                otherColumns.forEach(c -> c.setVisible(false));
            }
        });
    }

    private void otherColumnBinding() {

        otherColumns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                otherCheckBox.setDisable(otherColumns.size() <= 0);

                boolean allVisible = true;

                for (AssessmentColumn<Student, ?> c : otherColumns) {

                    if (!c.isVisible()) {
                        allVisible = false;
                        break;
                    }
                }
                otherCheckBox.setSelected(allVisible);
            }
        });
    }
//
//    private void totalColumnBinding(AssessmentColumn<Student, ?> totalColumn) {
//        ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();
//        checkBoxes.add(essaysCheckBox);
//        checkBoxes.add(essayPlansCheckBox);
//        checkBoxes.add(examsCheckBox);
//        checkBoxes.add(participationCheckBox);
//        checkBoxes.add(presentationsCheckBox);
//        checkBoxes.add(quizzesCheckBox);
//        checkBoxes.add(argAnalysesCheckBox);
//        checkBoxes.add(otherCheckBox);
//
//    }


    //Table Methods//
    @FXML
    public void editSurnameCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String surname = editedCell.getNewValue();

        if (selectedStudent == blankStudent) {
            courseManager.newStudent(blankStudent);
            newBlankStudent();
        }
        selectedStudent.setSurname(surname);
    }

    @FXML
    public void editGivenNamesCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String givenNames = editedCell.getNewValue();

        if (selectedStudent == blankStudent) {
            courseManager.newStudent(blankStudent);
            newBlankStudent();
        }
        selectedStudent.setGivenNames(givenNames);
    }

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
    @FXML
    public void importStudents(ActionEvent event) {
        Window window = importButton.getScene().getWindow();
        List<File> files = FileChooserWindow.displayImportWindow(window, "Choose a file to import from", true);

        for (File file : files) {
            courseManager.newStudents(StudentImporter.importStudents(file));
        }

        //TODO: assign assessments (if any) to students
    }

    @FXML
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

    public void setupAssessments(ObservableList<Assessment> assessments) {
        Map<Boolean, List<Assessment>> split = assessments.stream().collect(Collectors.partitioningBy(a -> a instanceof StdAssessment));
        List<StdAssessment> stdAssessments = split.get(true).stream().map(a -> (StdAssessment) a).collect(Collectors.toList());
        List<AssessmentSet> assessmentSets = split.get(false).stream().map(a -> (AssessmentSet) a).collect(Collectors.toList());
        ;

        for (StdAssessment std : stdAssessments) {
            courseManager.assignAssessment(std);
            blankStudent.addStdAssessmentData(std);
            createStdAssessmentColumn(std);
        }

        for (AssessmentSet set : assessmentSets) {
            courseManager.assignAssessment(set);
            blankStudent.addAssessmentSetData(set);
            createAssessmentSetColumns(set);
        }

        createTotalColumn();
        addAssessmentsButton.setDisable(true);
    }

    private void createStdAssessmentColumn(StdAssessment std) {
        AssessmentColumn<Student, Integer> column = new AssessmentColumn<>(std.getName(), std);
        column.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(std));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        table.getColumns().add(column);
        addToColumnsList(column);
    }

    private void createAssessmentSetColumns(AssessmentSet assessmentSet) {
        for (StdAssessment std : assessmentSet.getStdAssessments()) {

            AssessmentColumn<Student, Integer> column = new AssessmentColumn<>(std.getName(), std);
            column.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(assessmentSet, std));
            column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            table.getColumns().add(column);
            addToColumnsList(column);
        }
        AssessmentColumn<Student, Double> totalColumn = new AssessmentColumn<>(assessmentSet.getName() + " Total", assessmentSet);
        totalColumn.setCellValueFactory(c -> c.getValue().assessmentSetTotalGradeProperty(assessmentSet));

        table.getColumns().add(totalColumn);
        addToColumnsList(totalColumn);
    }

    private void createTotalColumn() {
        AssessmentColumn<Student, Double> totalColumn = new AssessmentColumn<Student, Double>("Total Mark");
        totalColumn.setCellValueFactory(c -> c.getValue().totalGradeProperty());

        table.getColumns().add(totalColumn);
    }

    private void addToColumnsList(AssessmentColumn<Student, ?> column) {
        AssessmentType type = column.getAssessment().getType();

        switch (type) {
            case ESSAY:
                essayColumns.add(column);
                break;
            case ESSAY_PLAN:
                essayPlanColumns.add(column);
                break;
            case EXAM:
                examColumns.add(column);
                break;
            case QUIZ:
                quizColumns.add(column);
                break;
            case ARG_ANALYSIS:
                argAnalysisColumns.add(column);
                break;
            case PARTICIPATION:
                participationColumns.add(column);
                break;
            case PRESENTATION:
                presentationColumns.add(column);
                break;
            case OTHER:
                otherColumns.add(column);
                break;
        }

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

//        StdAssessment essay = new StdAssessment("Essay", AssessmentType.ESSAY, 0.4);
//        StdAssessment exam = new StdAssessment("Exam", AssessmentType.EXAM, 0.4);
//        AssessmentSet quiz = new AssessmentSet("Quiz", AssessmentType.QUIZ, 0.2, 5, 4);
////        assignedAssessments.addAssessment(essay);
////        assignedAssessments.addAssessment(exam);
////        assignedAssessments.addAssessmentSet(quiz);
//
////        fred.addStdAssessmentData(essay);
////        fred.addStdAssessmentData(exam);
//        fred.addAssessmentSetData(quiz);
//
////        mary.addStdAssessmentData(essay);
////        mary.addStdAssessmentData(exam);
//        mary.addAssessmentSetData(quiz);
//
////        jane.addStdAssessmentData(essay);
////        jane.addStdAssessmentData(exam);
//        jane.addAssessmentSetData(quiz);
//
////        blankStudent.addStdAssessmentData(essay);
////        blankStudent.addStdAssessmentData(exam);
//        blankStudent.addAssessmentSetData(quiz);
//
////        AssessmentColumn<Student, Integer> essayColumn = new AssessmentColumn<>(essay.getName(), essay);
////        essayColumn.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(essay));
////        essayColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
////        table.getColumns().add(essayColumn);
////
////        AssessmentColumn<Student, Integer> examColumn = new AssessmentColumn<>(exam.getName(), exam);
////        examColumn.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(exam));
////        examColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
////        table.getColumns().add(examColumn);
//
//        for (StdAssessment q: quiz.getStdAssessments()) {
//            AssessmentColumn<Student, Integer> quizColumn = new AssessmentColumn<>(q.getName(), q);
//            quizColumn.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(quiz, q));
//            quizColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//            table.getColumns().add(quizColumn);
//        }
//
//        AssessmentColumn<Student, Double> quizTotalColumn = new AssessmentColumn<>(quiz.getName() + " Total", quiz);
//        quizTotalColumn.setCellValueFactory(c -> c.getValue().assessmentSetTotalGradeProperty(quiz));
//        table.getColumns().add(quizTotalColumn);
//
//        AssessmentColumn<Student, Double> totalColumn = new AssessmentColumn<>("Total Mark");
//        totalColumn.setCellValueFactory(c -> c.getValue().totalGradeProperty());
//        table.getColumns().add(totalColumn);

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


