package gradebook;

import gradebook.enums.AssessmentType;
import gradebook.enums.Gender;
import gradebook.enums.Grade;
import gradebook.model.Class;
import gradebook.model.*;
import gradebook.tools.FileChooserWindow;
import gradebook.tools.FileManager;
import gradebook.tools.StudentCloner;
import gradebook.tools.StudentImporter;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    //Main Layout//
    @FXML
    BorderPane mainPane;

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
    //Edit//
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

    //View//
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
    private ComboBox<StudentGroup> classListBox;
    @FXML
    private ComboBox<Grade> gradeListBox;

    @FXML
    private ToggleButton studentViewToggleButton;
    @FXML
    private ToggleButton anonymiseToggleButton;

    @FXML
    private Button addAssessmentsButton;

    //Statistics//
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab statisticsTab;

    @FXML
    private ComboBox<StudentGroup> statisticsClassComboBox;
    @FXML
    private ComboBox<AssessmentColumn<Student, ?>> columnComboBox;

    @FXML
    private Label medianLabel;
    @FXML
    private Label averageLabel;
    @FXML
    private Label highestLabel;
    @FXML
    private Label lowestLabel;

    StatisticsPane statisticsPane;



    //Footer Bar//
    @FXML
    private Label numberOfStudentsLabel;
    @FXML
    private Label numberSelectedLabel;


    //Control//
    private ObservableList<AssessmentColumn<Student, ?>> essayColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> examColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> essayPlanColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> participationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> argAnalysisColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> quizColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> presentationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private ObservableList<AssessmentColumn<Student, ?>> otherColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private AssessmentColumn<Student, Double> totalColumn;

    private CourseManager courseManager = new CourseManager("PHIL1011");    //TODO: request course name
    private Student blankStudent;
    private ObservableList<Student> clipBoardStudents = FXCollections.observableArrayList();

    private Scene assessmentCreationWindow;
    private Stage stage;
    private FileManager fileManager;


    public void setCourseManager(CourseManager newCourseManager) {
        this.courseManager = newCourseManager;
        setupClassListBox();
        setupStatisticsClassBox();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTabPaneSettings();

        table.getItems().addAll(courseManager.getAllStudents());
//        addDummyData();
        newBlankStudent();

        loadTableSettings();
        setupColumns();
        populateColumns();

        loadToolbarSettings();
        setupToolbarBindings();

        loadFooterSettings();

//        showStatisticsPane();
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

    private void loadTabPaneSettings() {
        statisticsTab.selectedProperty().addListener(obs -> {

            if (statisticsTab.isSelected()) {
                tabPane.getStyleClass().add("special-tab-pane");

            } else {
                tabPane.getStyleClass().remove("special-tab-pane");
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

    private void loadFooterSettings() {
        numberSelectedLabel.setText("");
        numberOfStudentsLabel.setText("Number of students: 0");

        table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                updateNumberSelectedLabel();
            }
        });

        table.getItems().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                updateNumberSelectedLabel();
                updateNumberOfStudentsLabel();
            }
        });
    }

    private void updateNumberSelectedLabel() {
        int n = table.getSelectionModel().getSelectedItems().size();

        if (table.getSelectionModel().getSelectedItems().contains(blankStudent)) {
            n = n - 1;
        }

        if (n > 0) {
            numberSelectedLabel.setText("Selected students: " + n);
        } else {
            numberSelectedLabel.setText("");
        }

    }

    private void updateNumberOfStudentsLabel() {
        int n = table.getItems().size();

        if (table.getItems().contains(blankStudent)) {
            n = n - 1;
        }
        numberOfStudentsLabel.setText("Number of students: " + n);
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

                        if (!className.isBlank()) {
                            Class newClass = new Class(className);
                            courseManager.addClass(newClass);
                            return newClass;
                        } else {
                            return null;
                        }
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

        setupFilterBoxes();
//        setupClassListBox();
//        setupGradeListBox();
        setupStatisticsLabels();
        setupStatisticsClassBox();
        columnsComboBoxBinding();

        statisticsClassBoxBinding();
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
                }
                ;
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
        //TODO: fix issue with checkboxes not disabling on removal of assessment

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
                quizzesCheckBox.setSelected(true);
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

    private void setupFilterBoxes() {
        setupClassListBox();
        setupGradeListBox();

        classListBoxBinding();
        gradeListBoxBinding();
    }

    private void setupClassListBox() {
        classListBox.setItems(courseManager.getStudentGroups());
        classListBox.getSelectionModel().selectFirst();
    }


    private void setupGradeListBox() {
        List<Grade> grades = Arrays.asList(Grade.values());
        gradeListBox.getItems().addAll(grades);
        gradeListBox.getSelectionModel().selectFirst();
    }

    private void classListBoxBinding() {
        classListBox.getSelectionModel().selectedItemProperty().addListener(obs -> {
            filterByGroupSelection();
            filterByGradeSelection();
        });
    }

    private void gradeListBoxBinding() {
        gradeListBox.getSelectionModel().selectedItemProperty().addListener(obs -> {
            filterByGroupSelection();
            filterByGradeSelection();
        });
    }

    private void filterByGroupSelection() {
        StudentGroup selectedGroup = classListBox.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            table.getItems().clear();
            table.getItems().addAll(selectedGroup.getStudents());
            table.getItems().add(blankStudent);
        }

    }

    private void filterByGradeSelection() {
        Grade selectedGrade = gradeListBox.getSelectionModel().getSelectedItem();
        StudentGroup selectedGroup = classListBox.getSelectionModel().getSelectedItem();

        if (selectedGrade != null) {
            switch (selectedGrade) {
                case ANY:
                    break;
                case HD:
//                    ObservableList<Student> nonHDStudents = selectedGroup.getStudents().filtered(s -> !selectedGroup.getTotalStatistics().getHDStudents().contains(s));
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getHDStudents().contains(s));
//                    removeIf(s -> {
//                        if (s.getTotalGrade() == null) {
//                            return true;
//                        } else {
//                            return !(s.getTotalGrade() <= 100 && s.getTotalGrade() >= 85);
//                        }
//                    });
                    break;
                case D:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getDStudents().contains(s));
                    break;
                case CR:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getCRStudents().contains(s));
                    break;
                case P:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getPStudents().contains(s));
                    break;
                case F:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getFStudents().contains(s));
                    break;
            }
        }
    }

    private void setupStatisticsLabels() {
        StudentGroup selectedGroup = statisticsClassComboBox.getSelectionModel().getSelectedItem();
        AssessmentColumn<Student, ?> selectedColumn = columnComboBox.getSelectionModel().getSelectedItem();

        if (selectedGroup != null && selectedColumn != null) {

            if (selectedColumn == totalColumn) {
                medianLabel.textProperty().bind(
                        Bindings.when(selectedGroup.totalGradeMedianProperty().asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.totalGradeMedianProperty().asString())
                );
                averageLabel.textProperty().bind(
                        Bindings.when(selectedGroup.totalGradeAverageProperty().asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.totalGradeAverageProperty().asString())
                );
                highestLabel.textProperty().bind(
                        Bindings.when(selectedGroup.totalGradeHighestProperty().asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.totalGradeHighestProperty().asString())
                );
                lowestLabel.textProperty().bind(
                        Bindings.when(selectedGroup.totalGradeLowestProperty().asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.totalGradeLowestProperty().asString())
                );

            } else {
                Assessment assessment = selectedColumn.getAssessment();

                medianLabel.textProperty().bind(
                        Bindings.when(selectedGroup.assessmentMedianProperty(assessment).asString().isEqualTo("null"))
                        .then("N/A")
                        .otherwise(selectedGroup.assessmentMedianProperty(assessment).asString())
                );
                averageLabel.textProperty().bind(
                        Bindings.when(selectedGroup.assessmentAverageProperty(assessment).asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.assessmentAverageProperty(assessment).asString())
                );
               highestLabel.textProperty().bind(
                        Bindings.when(selectedGroup.assessmentHighestProperty(assessment).asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.assessmentHighestProperty(assessment).asString())
                );
                lowestLabel.textProperty().bind(
                        Bindings.when(selectedGroup.assessmentLowestProperty(assessment).asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.assessmentLowestProperty(assessment).asString())
                );
            }
        }

        medianLabel.textProperty().addListener(obs -> {
            if (medianLabel.getText() != null && !medianLabel.getText().equals("null")) {
                try {
                    if (Double.parseDouble(medianLabel.getText()) >= 70) {
                        medianLabel.setTextFill(Color.GREEN);
                    } else {
                        medianLabel.setTextFill(Color.RED);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void columnsComboBoxBinding() {
        this.columnComboBox.getSelectionModel().selectedItemProperty().addListener(obs -> {
            setupStatisticsLabels();
            updateBarChart();
        });
    }


    private void statisticsClassBoxBinding() {
        statisticsClassComboBox.getSelectionModel().selectedItemProperty().addListener(obs -> {
            setupStatisticsLabels();
            updateBarChart();
        });
    }

    private void setupStatisticsClassBox() {
        statisticsClassComboBox.setItems(courseManager.getStudentGroups());
        statisticsClassComboBox.getSelectionModel().selectFirst();
    }

    private void updateBarChart() {
        if (statisticsPane != null) {
            statisticsPane.replaceAndFillBarChart(courseManager, totalColumn, statisticsClassComboBox, columnComboBox);
        }
    }


    //Table Methods//
    public void addAllStudentsToTable(ObservableList<Student> students) {
        table.getItems().clear();
        table.getItems().add(blankStudent);
        table.getItems().addAll(students);
        table.sort();
    }

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

    public void clearTable() {
        blankStudent = new Student();
        removeAssessmentColumns();
    }

    private void removeAssessmentColumns() {
        table.getColumns().removeIf(column -> column instanceof AssessmentColumn);
    }

    public void removeTotalColumn() {
        columnComboBox.getSelectionModel().clearSelection();
        table.getColumns().remove(totalColumn);
    }

    public void reAddTotalColumn() {
        table.getColumns().add(totalColumn);
        columnComboBox.getSelectionModel().select(totalColumn);
    }


    //Toolbar Methods//
    public void loadGradebook() {
//        Window window = loadMenuItem.getParentPopup().getScene().getWindow();
        Stage stage = (Stage) table.getParent().getScene().getWindow();

        File file = FileChooserWindow.displayLoadWindow(stage, "Load...");

        fileManager = new FileManager();

        //TODO: warning about unsaved file before loading

        fileManager.load(file, this);

        //TODO: allow save (rather than just saveas) after loading
        saveMenuItem.setDisable(false);
    }

    public void saveGradebook() {

        if (fileManager == null) {
            System.out.println("No file to save!");

        } else {
            fileManager.save(courseManager.getCourseName(), courseManager.getClasses());
        }
    }

    public void saveGradebookAs() {
        Stage stage = (Stage) table.getParent().getScene().getWindow();

        File file = FileChooserWindow.displaySaveWindow(stage, "Save As...");

        fileManager = new FileManager();

        fileManager.saveAs(file, courseManager.getCourseName(), courseManager.getClasses());
        saveMenuItem.setDisable(false);
    }

    @FXML
    public void importStudents(ActionEvent event) {
        Window window = importButton.getScene().getWindow();
        List<File> files = FileChooserWindow.displayImportWindow(window, "Choose a file to import from", true);

        for (File file : files) {
            ObservableList<Student> students = StudentImporter.importStudents(file);
            courseManager.newStudents(students);

//            table.getItems().remove(blankStudent);
            table.getItems().addAll(students);
//            table.getItems().add(blankStudent);
            table.sort();
        }
    }

    @FXML
    public void copyStudents() {
        clipBoardStudents.clear();
        ObservableList<Student> toCopy = table.getSelectionModel().getSelectedItems();
        clipBoardStudents.addAll(StudentCloner.run(toCopy));
    }

    @FXML
    public void cutStudents() {
        clipBoardStudents.clear();
        ObservableList<Student> toCut = table.getSelectionModel().getSelectedItems();
        clipBoardStudents.addAll(StudentCloner.run(toCut));

        for (Student s : toCut) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(toCut);
    }

    @FXML
    public void pasteStudents() {
        int index = table.getSelectionModel().getSelectedIndex();
        courseManager.reAddAllStudentsAt(index, clipBoardStudents);
        table.getItems().addAll(index, clipBoardStudents);

    }

    @FXML
    public void deleteStudents() {
        List<Student> selected = new ArrayList<>(table.getSelectionModel().getSelectedItems());
        selected.remove(blankStudent);

        for (Student s : selected) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(selected);
    }

    @FXML
    public void selectAll() {
        table.requestFocus();
        table.getSelectionModel().selectAll();
        table.getSelectionModel().clearSelection(table.getItems().size() - 1);
    }

    @FXML
    public void selectNone() {
        table.getSelectionModel().clearSelection();
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

    @FXML
    public void displayAssessmentEditWindow() throws IOException {

        stage.setTitle("Modify Assessments");
        stage.showAndWait();
    }

    public void setupAllAssessments(ObservableList<Assessment> assessments) {
        Map<Boolean, List<Assessment>> split = assessments.stream().collect(Collectors.partitioningBy(a -> a instanceof StdAssessment));
        List<StdAssessment> stdAssessments = split.get(true).stream().map(a -> (StdAssessment) a).collect(Collectors.toList());
        List<AssessmentSet> assessmentSets = split.get(false).stream().map(a -> (AssessmentSet) a).collect(Collectors.toList());

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

    public void setupStdAssessment(StdAssessment stdAssessment) {
        courseManager.assignAssessment(stdAssessment);
        blankStudent.addStdAssessmentData(stdAssessment);
        createStdAssessmentColumn(stdAssessment);
    }

    public void setupAssessmentSet(AssessmentSet assessmentSet) {
        courseManager.assignAssessment(assessmentSet);
        blankStudent.addAssessmentSetData(assessmentSet);
        createAssessmentSetColumns(assessmentSet);
    }

    public void removeAssessment(Assessment assessment) {
        courseManager.unassignAssessment(assessment);
        blankStudent.removeAssessmentData(assessment);
        removeAssessmentColumn(assessment);
        removeTotalColumn();
    }

//    public void setupStdAssessments(ObservableList<StdAssessment> stdAssessments) {
//        for (StdAssessment std : stdAssessments) {
//            courseManager.assignAssessment(std);
//            blankStudent.addStdAssessmentData(std);
//            createStdAssessmentColumn(std);
//
//            addAssessmentsButton.setDisable(true);
//        }
//    }
//
//    public void setupAssessmentSets(ObservableList<AssessmentSet> assessmentSets) {
//        for (AssessmentSet set : assessmentSets) {
//            courseManager.assignAssessment(set);
//            blankStudent.addAssessmentSetData(set);
//            createAssessmentSetColumns(set);
//        }
//
//        addAssessmentsButton.setDisable(true);
//    }

    private void createStdAssessmentColumn(StdAssessment std) {
        AssessmentColumn<Student, Integer> column = new AssessmentColumn<>(std.getName(), std);
        column.textProperty().bind(std.nameProperty());
        column.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(std));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column.setPrefWidth(62);

        table.getColumns().add(column);
        addToColumnsList(column);

        this.columnComboBox.getItems().add(column);
    }

    private void createAssessmentSetColumns(AssessmentSet assessmentSet) {
        for (StdAssessment std : assessmentSet.getStdAssessments()) {

            AssessmentColumn<Student, Integer> column = new AssessmentColumn<>(std.getName(), std);
            column.textProperty().bind(std.nameProperty());
            column.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(assessmentSet, std));
            column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            column.setPrefWidth(62);

            table.getColumns().add(column);
            addToColumnsList(column);
        }
        AssessmentColumn<Student, Double> setColumn = new AssessmentColumn<>(assessmentSet.getName() + " Total", assessmentSet);
        setColumn.textProperty().bind(assessmentSet.nameProperty().concat(" Total"));
        setColumn.setCellValueFactory(c -> c.getValue().assessmentSetTotalGradeProperty(assessmentSet));
        setColumn.setPrefWidth(86);

        table.getColumns().add(setColumn);
        addToColumnsList(setColumn);

        this.columnComboBox.getItems().add(setColumn);
    }

    public void createTotalColumn() {
        if (totalColumn == null) {
            totalColumn = new AssessmentColumn<Student, Double>("Total Mark");
            totalColumn.setCellValueFactory(c -> c.getValue().totalGradeProperty());
            totalColumn.setPrefWidth(91);

            table.getColumns().add(totalColumn);

            this.columnComboBox.getItems().add(0, totalColumn);
            columnComboBox.getSelectionModel().select(totalColumn);

        } else {
            removeTotalColumn();
            reAddTotalColumn();
        }
    }

    private void removeAssessmentColumn(Assessment assessment) {
        AssessmentType type = assessment.getType();
        AssessmentColumn<Student, ?> column = null;

        switch (type) {
            case ESSAY:
                column = findAndRemoveAssessmentColumn(essayColumns, assessment);
                break;
            case ESSAY_PLAN:
                column = findAndRemoveAssessmentColumn(essayPlanColumns, assessment);
                break;
            case EXAM:
                column = findAndRemoveAssessmentColumn(examColumns, assessment);
                break;
            case QUIZ:
                column = findAndRemoveAssessmentColumn(quizColumns, assessment);
                break;
            case ARG_ANALYSIS:
                column = findAndRemoveAssessmentColumn(argAnalysisColumns, assessment);
                break;
            case PARTICIPATION:
                column = findAndRemoveAssessmentColumn(participationColumns, assessment);
                break;
            case PRESENTATION:
                column = findAndRemoveAssessmentColumn(presentationColumns, assessment);
                break;
            case OTHER:
                column = findAndRemoveAssessmentColumn(otherColumns, assessment);
                break;
        }

        if (column != null) {
            table.getColumns().remove(column);
        }
    }

    private AssessmentColumn<Student, ?> findAndRemoveAssessmentColumn(ObservableList<AssessmentColumn<Student, ?>> columnList, Assessment assessment) {
        AssessmentColumn<Student, ?> column = null;

        for (AssessmentColumn<Student, ?> c : columnList) {
            if (c.getAssessment() == assessment) {
                columnList.remove(c);
                column = c;
                break;
            }
        }
        return column;
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

    @FXML
    public void showStatisticsPane() {

        if (courseManager.getAssessments().size() < 1) {
            System.out.println("No assessments statistics found!");
            //TODO: popup message

        } else {
            if (statisticsPane == null) {
                statisticsPane = new StatisticsPane();

                setupCloseButton(statisticsPane.getCloseButton());

                mainPane.setRight(statisticsPane);
                statisticsPane.fillBarChart(courseManager, totalColumn, statisticsClassComboBox, columnComboBox);
                statisticsPane.fillPieChart(courseManager);
            } else {
//                statisticsPane = new StatisticsPane();
//
//                setupCloseButton(statisticsPane.getCloseButton());
//
                mainPane.setRight(statisticsPane);
//                statisticsPane.fillBarChart(courseManager, totalColumn, statisticsClassComboBox, columnComboBox);
//                statisticsPane.fillPieChart(courseManager);
            }
        }
    }

    private void setupCloseButton(Button closeButton) {
        closeButton.setOnAction(e -> {
            mainPane.setRight(null);
        });
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

        table.getItems().add(fred);
        table.getItems().add(mary);
        table.getItems().add(jane);

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
    public void printSelectedStudents() {
        table.getSelectionModel().getSelectedItems().forEach(System.out::println);
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

    @FXML
    public void printAssessmentStatistics() {

        for (StudentGroup c : classListBox.getItems()) {
            System.out.println(c.getName());
            c.getAssessments().forEach(a -> {
                System.out.println(a.getName() + ":");
                System.out.println("number of HDs = " + c.getStatistics(a).numberOfHDs());
                System.out.println("number of Ds = " + c.getStatistics(a).numberOfDs());
                System.out.println("number of CRs = " + c.getStatistics(a).numberOfCRs());
                System.out.println("number of Ps = " + c.getStatistics(a).numberOfPs());
                System.out.println("number of Fs = " + c.getStatistics(a).numberOfFs());
                System.out.println("Number of compeleted assessments = " + c.getNumberAttempted(a));
                System.out.println("Number of students = " + c.getNumberOfStudents());
                System.out.println();
            });

            System.out.println("Total Grade:");
            System.out.println("number of HDs = " + c.getTotalStatistics().numberOfHDs());
            System.out.println("number of Ds = " + c.getTotalStatistics().numberOfDs());
            System.out.println("number of CRs = " + c.getTotalStatistics().numberOfCRs());
            System.out.println("number of Ps = " + c.getTotalStatistics().numberOfPs());
            System.out.println("number of Fs = " + c.getTotalStatistics().numberOfFs());
            System.out.println();
        }
    }



}


