package net.mjduncan.gradebook;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.mjduncan.gradebook.commands.primitive_commands.RedoCommand;
import net.mjduncan.gradebook.commands.primitive_commands.SaveGradebookAsCommand;
import net.mjduncan.gradebook.commands.primitive_commands.SaveGradebookCommand;
import net.mjduncan.gradebook.commands.primitive_commands.UndoCommand;
import net.mjduncan.gradebook.commands.refresh_commands.LoadGradebookCommand;
import net.mjduncan.gradebook.commands.refresh_commands.NewGradebookCommand;
import net.mjduncan.gradebook.commands.standard_commands.AddNewStudentCommand;
import net.mjduncan.gradebook.commands.standard_commands.FinaliseAllAssessmentsCommand;
import net.mjduncan.gradebook.commands.standard_commands.FinaliseColumnCommand;
import net.mjduncan.gradebook.commands.standard_commands.ImportStudentsCommand;
import net.mjduncan.gradebook.commands.standard_commands.edit_commands.*;
import net.mjduncan.gradebook.commands.standard_commands.view_commands.*;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.enums.Gender;
import net.mjduncan.gradebook.enums.Grade;
import net.mjduncan.gradebook.model.*;
import net.mjduncan.gradebook.tools.CommandManager;
import net.mjduncan.gradebook.tools.CourseManager;
import net.mjduncan.gradebook.tools.FileChooserWindow;
import net.mjduncan.gradebook.tools.FileManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    //Main Layout//
    @FXML
    private HBox mainPane;

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
    private TableColumn<Student, ClassGroup> classColumn;
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


    @FXML
    private Button addAssessmentsButton;
    @FXML
    private Button modifyAssessmentsButton;
    @FXML
    private Button finaliseButton;

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
    @FXML
    private Label numberLabel;

    @FXML
    private Button statisticsButton;

    StatisticsPane statisticsPane;


    //Footer Bar//
    @FXML
    private Label numberOfStudentsLabel;
    @FXML
    private Label numberSelectedLabel;
    @FXML
    private Label statusLabel;


    //Controller Variables//
    private final ObservableList<AssessmentColumn<Student, ?>> essayColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> examColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> essayPlanColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> participationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> argAnalysisColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> quizColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> presentationColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private final ObservableList<AssessmentColumn<Student, ?>> otherColumns = FXCollections.observableArrayList((AssessmentColumn<Student, ?> col) -> new Observable[]{col.visibleProperty()});
    private AssessmentColumn<Student, Double> totalColumn;

    private final CourseManager courseManager = new CourseManager("Your Course");
    private Student blankStudent;
    private final ObservableList<Student> clipBoardStudents = FXCollections.observableArrayList();

    private final CommandManager commandManager = new CommandManager();

    private ChangeListener<AssessmentColumn<Student, ?>> columnsComboBoxListener;
    private ChangeListener<StudentGroup> statisticsClassComboBoxListener;

    private Scene assessmentCreationWindow;
    private AssessmentCreationController assessmentCreationController;
    private Stage stage;

    private final ObjectProperty<FileManager> fileManager = new SimpleObjectProperty<>(null);



    public CourseManager getCourseManager() {
        return courseManager;
    }

    public TableView<Student> getTable() {
        return table;
    }

    public ObservableList<Student> getClipBoardStudents() {
        return clipBoardStudents;
    }

    public Button getAddAssessmentsButton() {
        return addAssessmentsButton;
    }

    public Button getModifyAssessmentsButton() {
        return modifyAssessmentsButton;
    }

    public Student getBlankStudent() {
        return blankStudent;
    }

    public AssessmentCreationController getAssessmentCreationController() throws IOException {

        if (this.assessmentCreationController == null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("assessment-creation-window.fxml"));
            Parent root = loader.load();

            assessmentCreationController = loader.getController();
            assessmentCreationController.setMainController(this);
            assessmentCreationController.setCommandManager(commandManager);

            assessmentCreationWindow = new Scene(root);

            stage = new Stage();
            stage.setScene(assessmentCreationWindow);
            stage.setTitle("Create Assessments");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(false);
        }

        return assessmentCreationController;
    }

    public void initialize() {
        loadTabPaneSettings();

        loadTableSettings();
        setupColumns();
        populateColumns();

        loadToolbarSettings();
        setupToolbarBindings();

        loadFooterSettings();

        table.getItems().addAll(courseManager.getAllStudents());
        newBlankStudent();
    }

    //Initialize Methods//
    public void displayWelcomeWindow() {
        TextInputDialog popup = new TextInputDialog();

        popup.setTitle("Gradebook");
        popup.setHeaderText("Welcome to Gradebook");
        popup.setContentText("Please enter the name of your course:");
        popup.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());
        popup.getDialogPane().setStyle("-fx-graphic: null");

        if (popup.showAndWait().isPresent()) {
            String courseName = popup.getResult();
            courseManager.setCourseName(courseName);
        }
    }

    public void newBlankStudent() {
        blankStudent = new Student();
        table.getItems().add(blankStudent);
    }

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

        statusLabel.setText("");
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
        degreeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String string) {
                return string == null ? "" : string.trim();
            }

            @Override
            public String fromString(String string) {
                if (string.isBlank()) {
                    return null;

                } else if (string.trim().matches("(\\S+)@(\\S+)(.com)(.\\S\\S)?") || string.trim().matches("(\\S+)@(\\S+)(.edu)(.\\S\\S)?")) {
                    return string.trim();

                } else {
                    Toolkit.getDefaultToolkit().beep();
                    setStatusText("Not a valid email address", 4);
                    throw new IllegalArgumentException();
                }
            }
        }));

        sidColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return integer == null ? "" : String.format("%09d", integer);
            }

            @Override
            public Integer fromString(String string) {
                if (string.isBlank()) {
                    return null;

                } else if (!string.trim().matches("\\d+")) {
                    Toolkit.getDefaultToolkit().beep();
                    setStatusText("Student ID must be a number", 4);
                    throw new NumberFormatException();

                } else {
                    int intValue = Integer.parseInt(string);

                    if (intValue > 999999999 || intValue < 0) {
                        Toolkit.getDefaultToolkit().beep();
                        setStatusText("Student ID must be a (positive) 9-digit number", 4);
                        throw new NumberFormatException();
                    } else {
                        return intValue;
                    }
                }
            }
        }));

        classColumn.setCellFactory(c -> {
            ComboBoxTableCell<Student, ClassGroup> comboBoxCell = new ComboBoxTableCell<>(courseManager.getClasses());
            comboBoxCell.setComboBoxEditable(true);

            comboBoxCell.setConverter(new StringConverter<>() {
                @Override
                public String toString(ClassGroup classGroup) {
                    return classGroup == null ? "" : classGroup.getName();
                }

                @Override
                public ClassGroup fromString(String className) {
                    ClassGroup classGroup = courseManager.getClass(className);

                    if (classGroup != null) {
                        return classGroup;
                    } else {

                        if (!className.isBlank()) {
                            ClassGroup newClass = new ClassGroup(className.trim());
                            courseManager.addClass(newClass);
                            return newClass;
                        } else {
                            return null;
                        }
                    }
                }
            });
            ContextMenu classContextMenu = new ContextMenu();
            MenuItem renameClass = new MenuItem("Rename Class...");
            MenuItem removeClass = new MenuItem("Remove Class");

            renameClass.disableProperty().bind(comboBoxCell.itemProperty().isNull());
            removeClass.disableProperty().bind(comboBoxCell.itemProperty().isNull());

            renameClass.setOnAction(e -> renameClass(comboBoxCell));
            removeClass.setOnAction(e -> removeClass(comboBoxCell));

            classContextMenu.getItems().add(renameClass);
            classContextMenu.getItems().add(removeClass);
            comboBoxCell.setContextMenu(classContextMenu);

            return comboBoxCell;
        });

        genderColumn.setCellFactory(c -> new ComboBoxTableCell<>(Gender.values()));
    }

    private void renameClass(ComboBoxTableCell<Student, ClassGroup> comboBoxCell) {
        ClassGroup classGroup = comboBoxCell.getItem();

        TextInputDialog popup = new TextInputDialog(classGroup.getName());
        popup.setTitle("Rename Class");
        popup.setHeaderText("");
        popup.setContentText("Name:");

        DialogPane dialogPane = popup.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());
        popup.getDialogPane().getStyleClass().add("rename-pane");

        if (popup.showAndWait().isPresent()) {
            String newName = popup.getResult();
            classGroup.setName(newName);
            table.refresh();
        }
    }

    private void removeClass(ComboBoxTableCell<Student, ClassGroup> comboBoxCell) {
        ClassGroup selectedClass = comboBoxCell.getItem();
        removeClass(selectedClass);
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

        modifyAssessmentsButton.setDisable(true);

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

        saveMenuItem.disableProperty().bind(fileManager.isNull().or(commandManager.undoStackEmptyProperty()));

        toggleButtonBindings();
        checkBoxBindings();
        homeBarBindings();
        otherButtonBindings();

        setupFilterBoxes();
        setupStatisticsLabels();
        setupStatisticsClassBox();

        setColumnsComboBoxListener();
        setStatisticsClassBoxListener();
    }

    private void otherButtonBindings() {
        finaliseButton.disableProperty().bind(Bindings.size(courseManager.getAssessments()).lessThan(1));
        statisticsButton.disableProperty().bind(Bindings.size(courseManager.getAssessments()).lessThan(1));
    }

    private void homeBarBindings() {
        copyButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        cutButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

//        IntegerBinding clipBoardSize = Bindings.size(clipBoardStudents);
//        BooleanBinding studentsInClipBoard = clipBoardSize.greaterThan(0);
        pasteButton.disableProperty().bind(Bindings.size(clipBoardStudents).lessThan(1));

        deleteButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull()
                .or(table.getSelectionModel().selectedItemProperty().isEqualTo(blankStudent)));

        selectAllButton.disableProperty().bind(Bindings.size(courseManager.getAllStudents()).lessThan(1));
        selectNoneButton.disableProperty().bind(Bindings.size(courseManager.getAllStudents()).lessThan(1).
                or(table.getSelectionModel().selectedItemProperty().isNull()
                        .or(table.getSelectionModel().selectedItemProperty().isEqualTo(blankStudent))));

        undoButton.disableProperty().bind(commandManager.undoStackEmptyProperty());
        redoButton.disableProperty().bind(commandManager.redoStackEmptyProperty());
    }

    private void toggleButtonBindings() {

        BooleanProperty allVisible = new SimpleBooleanProperty();
        allVisible.bind(surnameColumn.visibleProperty()
                .and(givenNamesColumn.visibleProperty()
                        .and(preferredNameColumn.visibleProperty()
                                .and(genderColumn.visibleProperty()
                                        .and(sidColumn.visibleProperty()
                                                .and(degreeColumn.visibleProperty()
                                                        .and(degreeColumn.visibleProperty()
                                                                .and(emailColumn.visibleProperty()
                                                                        .and(classColumn.visibleProperty()))))))))
        );


        allVisible.addListener((obs -> {
            if (allVisible.getValue() && !studentViewToggleButton.isSelected()) {
                studentViewToggleButton.setSelected(true);
            }

            if (!allVisible.getValue() && studentViewToggleButton.isSelected()) {
                studentViewToggleButton.setSelected(false);
            }
        }));

        surnameColumn.visibleProperty().addListener(obs -> {
            allVisible.getValue();                          //Strange bug(?) without this part. allVisible listener does not trigger//
            if (surnameColumn.isVisible() && anonymiseToggleButton.isSelected()) {
                anonymiseToggleButton.setSelected(false);
            }
        });

        givenNamesColumn.visibleProperty().addListener(obs -> {
            if (givenNamesColumn.isVisible() && anonymiseToggleButton.isSelected()) {
                anonymiseToggleButton.setSelected(false);
            }
        });

        preferredNameColumn.visibleProperty().addListener(obs -> {
            if (preferredNameColumn.isVisible() && anonymiseToggleButton.isSelected()) {
                anonymiseToggleButton.setSelected(false);
            }
        });

        sidColumn.visibleProperty().addListener(obs -> {
            if (sidColumn.isVisible() && anonymiseToggleButton.isSelected()) {
                anonymiseToggleButton.setSelected(false);
            }
        });

        emailColumn.visibleProperty().addListener(obs -> {
            if (emailColumn.isVisible() && anonymiseToggleButton.isSelected()) {
                anonymiseToggleButton.setSelected(false);
            }
        });
    }

    private void checkBoxBindings() {
        assessmentColumnBindings(essayColumns, essaysCheckBox);
        assessmentColumnBindings(examColumns, examsCheckBox);
        assessmentColumnBindings(essayPlanColumns, essayPlansCheckBox);
        assessmentColumnBindings(argAnalysisColumns, argAnalysesCheckBox);
        assessmentColumnBindings(participationColumns, participationCheckBox);
        assessmentColumnBindings(quizColumns, quizzesCheckBox);
        assessmentColumnBindings(presentationColumns, presentationsCheckBox);
        assessmentColumnBindings(otherColumns, otherCheckBox);
    }

    private void assessmentColumnBindings(ObservableList<AssessmentColumn<Student, ?>> columns, CheckBox checkBox) {
        columns.addListener(new ListChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends AssessmentColumn<Student, ?>> change) {

                if (columns.size() <= 0) {
                    checkBox.setSelected(false);
                    checkBox.setDisable(true);

                } else {
                    checkBox.setDisable(false);
                }


                boolean allVisible = true;

                if (columns.size() > 0) {
                    for (AssessmentColumn<Student, ?> c : columns) {

                        if (!c.isVisible()) {
                            allVisible = false;
                            break;
                        }
                    }
                } else {
                    allVisible = false;
                }

                checkBox.setSelected(allVisible);
            }
        });
    }

    private void setupFilterBoxes() {
        setupClassListBox();
        setupGradeListBox();
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

    public void setupStatisticsLabels() {
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
                numberLabel.textProperty().bind(
                        Bindings.when(selectedGroup.numberAttemptedProperty().asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.numberAttemptedProperty().asString())
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
                numberLabel.textProperty().bind(
                        Bindings.when(selectedGroup.numberAttemptedProperty(assessment).asString().isEqualTo("null"))
                                .then("N/A")
                                .otherwise(selectedGroup.numberAttemptedProperty(assessment).asString())
                );

            }

        } else {
            medianLabel.textProperty().unbind();
            medianLabel.setText("N/A");

            averageLabel.textProperty().unbind();
            averageLabel.setText("N/A");

            highestLabel.textProperty().unbind();
            highestLabel.setText("N/A");

            lowestLabel.textProperty().unbind();
            lowestLabel.setText("N/A");

            numberLabel.textProperty().unbind();
            numberLabel.setText("N/A");
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

    public void setColumnsComboBoxListener() {
        columnsComboBoxListener = new ChangeListener<AssessmentColumn<Student, ?>>() {
            @Override
            public void changed(ObservableValue<? extends AssessmentColumn<Student, ?>> observableValue, AssessmentColumn<Student, ?> oldValue, AssessmentColumn<Student, ?> newValue) {
                StudentGroup group = statisticsClassComboBox.getSelectionModel().getSelectedItem();

                if (oldValue != null && newValue != null && group != null) {
                    commandManager.execute(new StatisticsFilterCommand(MainController.this, statisticsClassComboBox, columnComboBox, group, oldValue), true);
                }
            }
        };

        this.columnComboBox.getSelectionModel().selectedItemProperty().addListener(columnsComboBoxListener);
    }


    public void setStatisticsClassBoxListener() {
        statisticsClassComboBoxListener = new ChangeListener<StudentGroup>() {
            @Override
            public void changed(ObservableValue<? extends StudentGroup> observableValue, StudentGroup oldValue, StudentGroup newValue) {
                AssessmentColumn<Student, ?> column = columnComboBox.getSelectionModel().getSelectedItem();

                if (oldValue != null && column != null) {
                    commandManager.execute(new StatisticsFilterCommand(MainController.this, statisticsClassComboBox, columnComboBox, oldValue, column), true);
                }
            }
        };

        statisticsClassComboBox.getSelectionModel().
        selectedItemProperty().
        addListener(statisticsClassComboBoxListener);
    }

    private void setupStatisticsClassBox() {
        statisticsClassComboBox.setItems(courseManager.getStudentGroups());
        statisticsClassComboBox.getSelectionModel().selectFirst();
    }

    public void removeColumnsComboBoxListener() {
        if (columnsComboBoxListener != null) {
            columnComboBox.getSelectionModel().selectedItemProperty().removeListener(columnsComboBoxListener);
        }
    }

    public void removeStatisticsClassBoxListener() {
        if (statisticsClassComboBoxListener != null) {
            statisticsClassComboBox.getSelectionModel().selectedItemProperty().removeListener(statisticsClassComboBoxListener);
        }
    }

    public void updateBarChart() {
        if (statisticsPane != null) {
            statisticsPane.replaceAndFillBarChart(courseManager, totalColumn, statisticsClassComboBox, columnComboBox);
        }
    }


    ////Controller Methods////

    public void handleKeyboardShortcuts(KeyEvent e) {
        KeyCombination copyKeys = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        KeyCombination cutKeys = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        KeyCombination pasteKeys = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

        if (copyKeys.match(e)) {
            copyButtonPressed();
        }
        if (cutKeys.match(e)) {
            cutButtonPressed();
        }
        if (pasteKeys.match(e)) {
            pasteButtonPressed();
        }
    }

    private ObservableList<AssessmentColumn<Student, ?>> getAssessmentColumnsByType(AssessmentType type) {
        ObservableList<AssessmentColumn<Student, ?>> assessmentColumns = FXCollections.observableArrayList();

        switch (type) {
            case ESSAY:
                assessmentColumns.addAll(essayColumns);
                return assessmentColumns;
            case ESSAY_PLAN:
                assessmentColumns.addAll(essayPlanColumns);
                return assessmentColumns;
            case EXAM:
                assessmentColumns.addAll(examColumns);
                return assessmentColumns;
            case QUIZ:
                assessmentColumns.addAll(quizColumns);
                return assessmentColumns;
            case ARG_ANALYSIS:
                assessmentColumns.addAll(argAnalysisColumns);
                return assessmentColumns;
            case PARTICIPATION:
                assessmentColumns.addAll(participationColumns);
                return assessmentColumns;
            case PRESENTATION:
                assessmentColumns.addAll(presentationColumns);
                return assessmentColumns;
            case OTHER:
                assessmentColumns.addAll(otherColumns);
                return assessmentColumns;
            default:
                return assessmentColumns;
        }
    }

    //Table Control Methods//
    public void addAllStudentsToTable(ObservableList<Student> students) {
        table.getItems().clear();
        addBlankStudent();
        table.getItems().addAll(students);
        table.sort();
    }

    public void addBlankStudent() {
        table.getItems().add(blankStudent);
    }

    @FXML
    public void editSurnameCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String newSurname = editedCell.getNewValue();
        String oldSurname = editedCell.getOldValue();

        if (selectedStudent == blankStudent) {
            selectedStudent.setSurname(newSurname);
            commandManager.execute(new AddNewStudentCommand(this, blankStudent), true);

        } else {
            commandManager.execute(new ChangeSurnameCommand(selectedStudent, oldSurname, newSurname), true);
        }
    }

    @FXML
    public void editGivenNamesCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String newGivenNames = editedCell.getNewValue();
        String oldGivenNames = editedCell.getOldValue();

        if (selectedStudent == blankStudent) {
            selectedStudent.setGivenNames(newGivenNames);
            commandManager.execute(new AddNewStudentCommand(this, blankStudent), true);

        } else {
            commandManager.execute(new ChangeGivenNamesCommand(selectedStudent, oldGivenNames, newGivenNames), true);
        }
    }

    @FXML
    public void editPreferredNameCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String newPreferredName = editedCell.getNewValue();
        String oldPreferredName = editedCell.getOldValue();

        if (selectedStudent == blankStudent) {
            selectedStudent.setPreferredName(newPreferredName);
            commandManager.execute(new AddNewStudentCommand(this, blankStudent), true);

        } else {
            commandManager.execute(new ChangePreferredNameCommand(selectedStudent, oldPreferredName, newPreferredName), true);
        }
    }

    @FXML
    public void editClassCell(TableColumn.CellEditEvent<Student, ClassGroup> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        ClassGroup newClassGroup = editedCell.getNewValue();
        ClassGroup oldClassGroup = editedCell.getOldValue();

        commandManager.execute(new ChangeClassGroupCommand(selectedStudent, oldClassGroup, newClassGroup), true);
    }

    @FXML
    public void editGenderCell(TableColumn.CellEditEvent<Student, Gender> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        Gender newGender = editedCell.getNewValue();
        Gender oldGender = editedCell.getOldValue();

        commandManager.execute(new ChangeGenderCommand(selectedStudent, oldGender, newGender), true);
    }

    @FXML
    public void editSIDCell(TableColumn.CellEditEvent<Student, Integer> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        Integer newSID = editedCell.getNewValue();
        Integer oldSID = editedCell.getOldValue();

        commandManager.execute(new ChangeSIDCommand(selectedStudent, oldSID, newSID), true);
    }

    @FXML
    public void editDegreeCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String newDegree = editedCell.getNewValue();
        String oldDegree = editedCell.getOldValue();

        commandManager.execute(new ChangeDegreeCommand(selectedStudent, oldDegree, newDegree), true);
    }

    @FXML
    public void editEmailCell(TableColumn.CellEditEvent<Student, String> editedCell) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        String newEmail = editedCell.getNewValue();
        String oldEmail = editedCell.getOldValue();

        commandManager.execute(new ChangeEmailCommand(selectedStudent, oldEmail, newEmail), true);
    }

    public void reset() {
        closeStatisticsPane();
        clearTable();
        removeAssessmentColumns();
        courseManager.clear();
        assessmentCreationController = null;
        addAssessmentsButton.setDisable(false);
    }

    private void clearTable() {
        table.getItems().clear();
        blankStudent = new Student();
    }

    private void removeAssessmentColumns() {
        table.getColumns().removeIf(column -> column instanceof AssessmentColumn);

        columnComboBox.getItems().clear();
    }

    public void removeTotalColumn() {
        columnComboBox.getSelectionModel().clearSelection();
        table.getColumns().remove(totalColumn);
    }

    public void reAddTotalColumn() {
        table.getColumns().add(totalColumn);
        columnComboBox.getSelectionModel().select(totalColumn);
    }


    //Toolbar Control Methods//

    //File Menu//
    @FXML
    public void newGradebookItemPressed() {
        Alert popup = new Alert(Alert.AlertType.CONFIRMATION, "Create New Gradebook?", ButtonType.YES, ButtonType.NO);
        popup.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());

        popup.setTitle("New Gradebook");
        popup.setHeaderText("Are you sure you want to create a new gradebook?");
        popup.setContentText("Any unsaved student data in the current gradebook will be lost.");

        if (popup.showAndWait().isPresent() && popup.getResult() == ButtonType.YES) {
            commandManager.execute(new NewGradebookCommand(this));
        }
    }

    @FXML
    public void loadGradebookItemPressed() {
        Alert popup = new Alert(Alert.AlertType.CONFIRMATION, "Load Gradebook?", ButtonType.YES, ButtonType.NO);
        popup.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());

        popup.setTitle("Load Gradebook");
        popup.setHeaderText("Are you sure you want to load a gradebook from file?");
        popup.setContentText("Any unsaved student data in the current gradebook will be lost.");

        if (popup.showAndWait().isPresent() && popup.getResult() == ButtonType.YES) {
            Stage stage = (Stage) table.getParent().getScene().getWindow();
            File file = FileChooserWindow.displayLoadWindow(stage, "Load Gradebook");

            commandManager.execute(new LoadGradebookCommand(this, statisticsPane, fileManager, file, stage));
        }
    }

    @FXML
    public void saveGradebookItemPressed() {
        if (fileManager.getValue() == null || fileManager == null) {
            System.out.println("No file to save!");

        } else {
            commandManager.execute(new SaveGradebookCommand(this, fileManager.getValue()) {
            });
        }
    }

    @FXML
    public void saveGradebookAsItemPressed() {
        Stage stage = (Stage) table.getParent().getScene().getWindow();
        File file = FileChooserWindow.displaySaveWindow(stage, "Save As...");

        commandManager.execute(new SaveGradebookAsCommand(this, fileManager, file, stage));
    }

    public void clearFileManager() {
        fileManager.set(null);
    }

    @FXML
    public void displayAssessmentCreationWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assessment-creation-window.fxml"));
        Parent root = loader.load();

        AssessmentCreationController assessmentSetupController = loader.getController();
        assessmentSetupController.setMainController(this);
        assessmentSetupController.setCommandManager(commandManager);

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

    public void removeAssessment(Assessment assessment) {
        courseManager.unassignAssessment(assessment);
        blankStudent.removeAssessmentData(assessment);
        removeAssessmentColumn(assessment);
        removeTotalColumn();
    }

    public void changeAssessmentSetQuantity(AssessmentSet assessmentSet, int newQuantity) {
        int oldQuantity = assessmentSet.getQuantity();

        System.out.println("Old Quantity: " + oldQuantity);
        System.out.println("New Quantity: " + newQuantity);

        if (newQuantity > oldQuantity) {

            int number = newQuantity - oldQuantity;

            for (int i = 0; i < number; i++) {
                String name = assessmentSet.getName() + " " + (assessmentSet.getQuantity() + 1);
                AssessmentType type = assessmentSet.getType();
                StdAssessment subAssessment = StdAssessment.newSubAssessment(name, type);

                courseManager.assignNewSubAssessment(assessmentSet, subAssessment);
                createSubAssessmentColumn(assessmentSet, subAssessment);
            }
        }

        if (newQuantity < oldQuantity) {
            int number = oldQuantity - newQuantity;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());

            alert.setTitle("Reduce Assessment Set Quantity");
            alert.setHeaderText("Are you sure you want to reduce the number of assessments for assessment set \"" + assessmentSet.getName() + "\" by " + number + "?");

            if (number > 1) {
                alert.setContentText("Reducing the number of assessments in a set by " + number + " will permanently remove the last "
                        + number + " assessments from the set. Any existing grades will be lost.");
            } else {
                alert.setContentText("Reducing the number of assessments in a set by " + number +
                        " will permanently remove the last assessment from the set. Any existing grades will be lost.");
            }

            if (alert.showAndWait().isPresent() && alert.getResult().equals(ButtonType.OK)) {
                List<SubAssessmentColumn<Student, ?>> subAssessmentColumns = getSubAssessmentColumns(assessmentSet);
                List<SubAssessmentColumn<Student, ?>> columnsToRemove = FXCollections.observableArrayList();

                List<StdAssessment> subAssessments = new ArrayList<>();

                for (int i = subAssessmentColumns.size() - number; i <= subAssessmentColumns.size() - 1; i++) {

                    SubAssessmentColumn<Student, ?> columnToRemove = subAssessmentColumns.get(i);
                    columnsToRemove.add(columnToRemove);

                    StdAssessment stdAssessment = (StdAssessment) columnToRemove.getAssessment();
                    subAssessments.add(stdAssessment);
                }

                table.getColumns().removeAll(columnsToRemove);
                subAssessments.forEach(this::removeAssessmentColumn);

                courseManager.unassignSubAssessments(assessmentSet, subAssessments);
            }

        }

    }

    public void createSubAssessmentColumn(AssessmentSet assessmentSet, StdAssessment subAssessment) {
        SubAssessmentColumn<Student, Integer> column = new SubAssessmentColumn<>(subAssessment.getName(), subAssessment, assessmentSet);
        column.textProperty().bind(subAssessment.nameProperty());
        column.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(assessmentSet, subAssessment));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column.setPrefWidth(62);

        addAssessmentColumnContextMenu(column);

        List<SubAssessmentColumn<Student, ?>> subAssessmentColumns = getSubAssessmentColumns(assessmentSet);
        SubAssessmentColumn<Student, ?> lastColumn = subAssessmentColumns.get(subAssessmentColumns.size() - 1);
        int lastColumnIndex = table.getColumns().indexOf(lastColumn);

        table.getColumns().add(lastColumnIndex + 1, column);
        addToColumnsList(column);
    }

    public List<SubAssessmentColumn<Student, ?>> getSubAssessmentColumns(AssessmentSet assessmentSet) {
        ObservableList<AssessmentColumn<Student, ?>> columnsOfType = getAssessmentColumnsByType(assessmentSet.getType());

        @SuppressWarnings("unchecked")
        List<SubAssessmentColumn<Student, ?>> subAssessmentColumns = columnsOfType.stream()
                .filter(c -> c instanceof SubAssessmentColumn)
                .map(c -> (SubAssessmentColumn<Student, ?>) c)
                .filter(c -> c.getParent() == assessmentSet)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        return subAssessmentColumns;
    }

    public void createStdAssessmentColumn(StdAssessment std) {
        AssessmentColumn<Student, Integer> column = new AssessmentColumn<>(std.getName(), std);
        column.textProperty().bind(std.nameProperty());
        column.setCellValueFactory(c -> c.getValue().stdAssessmentGradeProperty(std));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return integer == null ? "" : integer.toString();
            }

            @Override
            public Integer fromString(String string) {
                if (string.isBlank()) {
                    return null;

                } else if (!string.trim().matches("\\d+")) {
                    Toolkit.getDefaultToolkit().beep();
                    setStatusText("Grades must be integers", 4);
                    throw new NumberFormatException();

                } else {
                    int intValue = Integer.parseInt(string);

                    if (intValue < 0 || intValue > 100) {
                        Toolkit.getDefaultToolkit().beep();
                        setStatusText("Grades must be integers between 0 and 100", 4);
                        throw new NumberFormatException();
                    } else {
                        return intValue;
                    }
                }
            }
        }));
        column.setPrefWidth(62);

        column.setOnEditCommit(e -> {
            Integer oldValue = e.getOldValue();
            Integer newValue = e.getNewValue();
            Student selectedStudent = table.getSelectionModel().getSelectedItem();

            commandManager.execute(new ChangeGradeCommand(std, selectedStudent, oldValue, newValue), true);
        });

        table.getColumns().add(column);
        addToColumnsList(column);

        this.columnComboBox.getItems().add(column);

        addAssessmentColumnContextMenu(column);
    }

    public void removeStdAssessmentColumn(StdAssessment stdAssessment) {
        AssessmentType type = stdAssessment.getType();

        switch (type) {
            case ESSAY:
                removeStdAssessmentColumnFromList(essayColumns, stdAssessment);
                break;
            case ESSAY_PLAN:
                removeStdAssessmentColumnFromList(essayPlanColumns, stdAssessment);
                break;
            case EXAM:
                removeStdAssessmentColumnFromList(examColumns, stdAssessment);
                break;
            case QUIZ:
                removeStdAssessmentColumnFromList(quizColumns, stdAssessment);
                break;
            case ARG_ANALYSIS:
                removeStdAssessmentColumnFromList(argAnalysisColumns, stdAssessment);
                break;
            case PARTICIPATION:
                removeStdAssessmentColumnFromList(participationColumns, stdAssessment);
                break;
            case PRESENTATION:
                removeStdAssessmentColumnFromList(presentationColumns, stdAssessment);
                break;
            case OTHER:
                removeStdAssessmentColumnFromList(otherColumns, stdAssessment);
                break;
        }
    }

    private void removeStdAssessmentColumnFromList(ObservableList<AssessmentColumn<Student, ?>> columns, StdAssessment stdAssessment) {
        for (AssessmentColumn<Student, ?> c : columns) {
            if (c.getAssessment() == stdAssessment) {
                table.getColumns().remove(c);
                columns.remove(c);

                this.columnComboBox.getItems().remove(c);
                break;
            }
        }
    }

    public void createAssessmentSetColumns(AssessmentSet assessmentSet) {
        for (StdAssessment std : assessmentSet.getStdAssessments()) {

            SubAssessmentColumn<Student, Integer> column = new SubAssessmentColumn<>(std.getName(), std, assessmentSet);
            column.textProperty().bind(std.nameProperty());
            column.setCellValueFactory(c -> c.getValue().assessmentSetGradeProperty(assessmentSet, std));
            column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            column.setPrefWidth(62);

            addAssessmentColumnContextMenu(column);

            column.setOnEditCommit(e -> {
                Integer oldValue = e.getOldValue();
                Integer newValue = e.getNewValue();
                Student selectedStudent = table.getSelectionModel().getSelectedItem();

                commandManager.execute(new ChangeGradeCommand(assessmentSet, std, selectedStudent, oldValue, newValue), true);

            });

            table.getColumns().add(column);
            addToColumnsList(column);
        }

        AssessmentColumn<Student, Double> setColumn = new AssessmentColumn<>(assessmentSet.getName() + " Total", assessmentSet);
        setColumn.textProperty().bind(assessmentSet.nameProperty().concat(" Total"));
        setColumn.setCellValueFactory(c -> c.getValue().assessmentSetTotalGradeProperty(assessmentSet));
        setColumn.setPrefWidth(86);

        addAssessmentColumnContextMenu(setColumn);

        table.getColumns().add(setColumn);
        addToColumnsList(setColumn);

        this.columnComboBox.getItems().add(setColumn);
    }

    public void removeAssessmentSetColumns(AssessmentSet assessmentSet) {
        AssessmentType type = assessmentSet.getType();

        switch (type) {
            case ESSAY:
                removeSetColumnsFromList(essayColumns, assessmentSet);
                break;
            case ESSAY_PLAN:
                removeSetColumnsFromList(essayPlanColumns, assessmentSet);
                break;
            case EXAM:
                removeSetColumnsFromList(examColumns, assessmentSet);
                break;
            case QUIZ:
                removeSetColumnsFromList(quizColumns, assessmentSet);
                break;
            case ARG_ANALYSIS:
                removeSetColumnsFromList(argAnalysisColumns, assessmentSet);
                break;
            case PARTICIPATION:
                removeSetColumnsFromList(participationColumns, assessmentSet);
                break;
            case PRESENTATION:
                removeSetColumnsFromList(presentationColumns, assessmentSet);
                break;
            case OTHER:
                removeSetColumnsFromList(otherColumns, assessmentSet);
                break;
        }
    }

    private void removeSetColumnsFromList(ObservableList<AssessmentColumn<Student, ?>> columns, AssessmentSet set) {
        ObservableList<AssessmentColumn<Student, ?>> toRemove = FXCollections.observableArrayList();

        for (AssessmentColumn<Student, ?> c : columns) {

            if (c.getAssessment() == set) {
                table.getColumns().remove(c);
                toRemove.add(c);
                columnComboBox.getItems().remove(c);

            } else if (c instanceof SubAssessmentColumn) {
                SubAssessmentColumn<Student, ?> subAssessmentColumn = (SubAssessmentColumn<Student, ?>) c;

                if (subAssessmentColumn.getParent() == set) {
                    table.getColumns().remove(subAssessmentColumn);
                    toRemove.add(subAssessmentColumn);
                }
            }
        }
        columns.removeAll(toRemove);
    }

    private void addAssessmentColumnContextMenu(AssessmentColumn<Student, ?> column) {
        ContextMenu menu = new ContextMenu();
        MenuItem info = new MenuItem("Assessment Info");
        MenuItem rename = new MenuItem("Rename Assessment...");
        MenuItem finalise = new MenuItem("Set Incomplete to Zero");

        menu.getItems().add(info);
        menu.getItems().add(rename);
        menu.getItems().add(finalise);

        info.setOnAction(e -> displayAssessmentInfo(column));

        rename.setOnAction(e -> {
            String result = renameAssessment(column);
            if (result != null) {
                column.getAssessment().setName(result);
            }
        });

        finalise.setOnAction(e -> {
            commandManager.execute(new FinaliseColumnCommand(column, courseManager.getAllStudents()), true);
        });

        column.setContextMenu(menu);
    }

    private void displayAssessmentInfo(AssessmentColumn<Student, ?> column) {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("Assessment Info");
        popup.setHeaderText(column.getAssessment().getName());
        popup.getDialogPane().setContent(column.assessmentInfoBox());
        popup.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());
        popup.getDialogPane().getStyleClass().add("info-pane");
        popup.showAndWait();
    }

    private String renameAssessment(AssessmentColumn<Student, ?> column) {
        TextInputDialog popup = new TextInputDialog(column.getText());
        popup.setTitle("Rename Assessment");
        popup.setHeaderText("");
        popup.setContentText("Name:");

        DialogPane dialogPane = popup.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());
        popup.getDialogPane().getStyleClass().add("rename-pane");

        if (popup.showAndWait().isPresent()) {
            return popup.getResult();
        } else {
            return null;
        }
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

    public void removeAssessmentColumn(Assessment assessment) {
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

            if (column.getAssessment() instanceof AssessmentSet) {
                ObservableList<StdAssessment> subAssessments = ((AssessmentSet) column.getAssessment()).getStdAssessments();
                ObservableList<AssessmentColumn<Student, ?>> columnsOfType = getAssessmentColumnsByType(column.getAssessment().getType());

                for (StdAssessment s : subAssessments) {
                    for (AssessmentColumn<Student, ?> c : columnsOfType) {

                        if (c.getAssessment() == s) {
                            table.getColumns().remove(c);
                            removeAssessmentColumn(s);
                            break;
                        }
                    }
                }
            }
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

    public void removeClass(ClassGroup classGroup) {
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert popup = new Alert(Alert.AlertType.WARNING, "Remove " + classGroup + " from the gradebook?", ButtonType.OK, cancel);
        popup.setTitle("Remove Class");
        popup.setHeaderText("WARNING:\nRemoving a class will also remove any students in the class.");
        popup.getDialogPane().getStylesheets().add(getClass().getResource("dialog-pane.css").toExternalForm());

        if (popup.showAndWait().isPresent() && popup.getResult().equals(ButtonType.OK)) {
            table.getItems().removeAll(classGroup.getStudents());
            courseManager.removeClass(classGroup);
        }
    }

    @FXML
    public void showStatisticsPane() {

        if (courseManager.getAssessments().size() < 1) {
            System.out.println("No assessments statistics found!");

        } else {
            Stage stage = (Stage) statisticsButton.getParent().getScene().getWindow();

            if (statisticsPane == null) {
                statisticsPane = new StatisticsPane();
                statisticsPane.setMinWidth(stage.getWidth() / 4.35);

                statisticsPaneSizeSettings();
                setupCloseButton(statisticsPane.getCloseButton());

                mainPane.getChildren().add(statisticsPane);

                statisticsPane.fillBarChart(courseManager, totalColumn, statisticsClassComboBox, columnComboBox);
                statisticsPane.fillPieChart(courseManager);

            } else {
                mainPane.getChildren().add(statisticsPane);
            }
        }
    }

    public void closeStatisticsPane() {
        mainPane.getChildren().remove(statisticsPane);
        statisticsPane = null;
    }

    public void disableModifyAssessmentsButton() {
        modifyAssessmentsButton.setDisable(true);
    }

    private void setupCloseButton(Button closeButton) {
        closeButton.setOnAction(e -> {
            closeStatisticsPane();
        });
    }

    private void statisticsPaneSizeSettings() {
        statisticsPane.prefHeightProperty().bind(table.heightProperty());
    }

    public void setStatusText(String text, int duration) {
        statusLabel.setText(text);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), statusLabel);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            statusLabel.setText("");
            statusLabel.setOpacity(1);
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), e -> ft.play()));
        timeline.play();
    }



    ////User Actions////

    //Table Actions//

    //Toolbar Actions//
    @FXML
    public void importButtonPressed() {
        Window window = importButton.getScene().getWindow();
        commandManager.execute(new ImportStudentsCommand(this, window), true);
    }

    @FXML
    public void copyButtonPressed() {
        commandManager.execute(new CopyCommand(this), false);
    }

    @FXML
    public void cutButtonPressed() {
        commandManager.execute(new CutCommand(this), true);
    }

    @FXML
    public void pasteButtonPressed() {
        commandManager.execute(new PasteCommand(this), true);
    }

    @FXML
    public void deleteButtonPressed() {
        List<Student> selected = new ArrayList<>(table.getSelectionModel().getSelectedItems());
        selected.remove(blankStudent);

        commandManager.execute(new DeleteCommand(this, selected), true);
    }

    @FXML
    public void deleteKeyPressed(KeyEvent e) {

        if (e.getCode() == KeyCode.DELETE) {
            List<Student> selected = new ArrayList<>(table.getSelectionModel().getSelectedItems());
            selected.remove(blankStudent);

            commandManager.execute(new DeleteCommand(this, selected), true);
        }
    }

    @FXML
    public void undoButtonPressed() {
        commandManager.execute(new UndoCommand(commandManager));
    }

    @FXML
    public void redoButtonPressed() {
        commandManager.execute(new RedoCommand(commandManager));
    }

    @FXML
    public void finaliseButtonPressed() {
        commandManager.execute(new FinaliseAllAssessmentsCommand(courseManager.getAllStudents()), true);
    }

    @FXML
    public void selectAllButtonPressed() {
        commandManager.execute(new SelectAllCommand(table), false);
    }

    @FXML
    public void selectNoneButtonPressed() {
        commandManager.execute(new SelectNoneCommand(table), false);
    }

    @FXML
    public void assessmentCheckBoxClicked(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        boolean selected = checkBox.isSelected();

        if (selected) {
            if (checkBox == essaysCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(essayColumns), true);

            } else if (checkBox == essayPlansCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(essayPlanColumns), true);

            } else if (checkBox == examsCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(examColumns), true);

            } else if (checkBox == argAnalysesCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(argAnalysisColumns), true);

            } else if (checkBox == quizzesCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(quizColumns), true);

            } else if (checkBox == participationCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(participationColumns), true);

            } else if (checkBox == presentationsCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(presentationColumns), true);

            } else if (checkBox == otherCheckBox) {
                commandManager.execute(new ShowAssessmentColumnCommand(otherColumns), true);

            } else {
                throw new IllegalArgumentException("Assessment type does not exit!");
            }

        } else {
            if (checkBox == essaysCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(essayColumns), true);

            } else if (checkBox == essayPlansCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(essayPlanColumns), true);

            } else if (checkBox == examsCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(examColumns), true);

            } else if (checkBox == argAnalysesCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(argAnalysisColumns), true);

            } else if (checkBox == quizzesCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(quizColumns), true);

            } else if (checkBox == participationCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(participationColumns), true);

            } else if (checkBox == presentationsCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(presentationColumns), true);

            } else if (checkBox == otherCheckBox) {
                commandManager.execute(new HideAssessmentColumnCommand(otherColumns), true);

            } else {
                throw new IllegalArgumentException("Assessment type does not exit!");
            }
        }
    }

    @FXML
    public void studentInfoButtonPressed() {

        if (studentViewToggleButton.isSelected()) {
            ObservableList<TableColumn<Student, ?>> infoColumns = FXCollections.observableArrayList();

            infoColumns.add(surnameColumn);
            infoColumns.add(givenNamesColumn);
            infoColumns.add(preferredNameColumn);
            infoColumns.add(genderColumn);
            infoColumns.add(sidColumn);
            infoColumns.add(degreeColumn);
            infoColumns.add(emailColumn);
            infoColumns.add(classColumn);

            commandManager.execute(new StudentInfoViewOnCommand(infoColumns), true);

        } else {
            commandManager.execute(new StudentInfoViewOffCommand(emailColumn, sidColumn, degreeColumn), true);
        }
    }

    @FXML
    public void anonymiseButtonPressed() {
        ObservableList<TableColumn<Student, ?>> infoColumns = FXCollections.observableArrayList();

        infoColumns.add(surnameColumn);
        infoColumns.add(givenNamesColumn);
        infoColumns.add(preferredNameColumn);
        infoColumns.add(genderColumn);
        infoColumns.add(sidColumn);
        infoColumns.add(degreeColumn);
        infoColumns.add(emailColumn);

        if (anonymiseToggleButton.isSelected()) {
            commandManager.execute(new AnonymiseOnCommand(infoColumns), true);

        } else {
            infoColumns.remove(sidColumn);
            infoColumns.remove(degreeColumn);
            infoColumns.remove(emailColumn);
            commandManager.execute(new AnonymiseOffCommand(infoColumns), true);
        }
    }

    @FXML
    public void filterTableAction() {
        commandManager.execute(new FilterByClassAndGradeCommand(this, classListBox, gradeListBox), false);
    }
}


