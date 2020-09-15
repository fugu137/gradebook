package gradebook.commands.standard_commands.assessment_creation_commands;

import gradebook.MainController;
import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.AssessmentSet;
import gradebook.model.StdAssessment;
import gradebook.model.Student;
import gradebook.model.SubAssessmentColumn;
import gradebook.tools.CourseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.List;

public class RemoveSubAssessmentsSubCommand implements StandardCommand {

    private MainController mainController;
    private CourseManager courseManager;
    private TableView<Student> table;
    private AssessmentSet assessmentSet;
    private int numberToRemove;
    private ObservableList<StdAssessment> subAssessments;
    private StandardCommand addSubAssessmentsCommand;

    RemoveSubAssessmentsSubCommand(MainController mainController, AssessmentSet assessmentSet, int newQuantity) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();
        this.assessmentSet = assessmentSet;
        this.numberToRemove = assessmentSet.getQuantity() - newQuantity;
        this.subAssessments = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {
//        int oldQuantity = assessmentSet.getQuantity();
//        int numberToRemove = oldQuantity - newQuantity;

        if (displayAlertAndReturnResponse(numberToRemove) == ButtonType.OK) {

            List<SubAssessmentColumn<Student, ?>> subAssessmentColumns = mainController.getSubAssessmentColumns(assessmentSet);
            List<SubAssessmentColumn<Student, ?>> columnsToRemove = FXCollections.observableArrayList();

            for (int i = subAssessmentColumns.size() - numberToRemove; i <= subAssessmentColumns.size() - 1; i++) {

                SubAssessmentColumn<Student, ?> columnToRemove = subAssessmentColumns.get(i);
                columnsToRemove.add(columnToRemove);

                StdAssessment stdAssessment = (StdAssessment) columnToRemove.getAssessment();
                subAssessments.add(stdAssessment);
            }

            table.getColumns().removeAll(columnsToRemove);
            subAssessments.forEach(mainController::removeAssessmentColumn);

            courseManager.unassignSubAssessments(assessmentSet, subAssessments);
            mainController.getBlankStudent().removeSubAssessmentData(assessmentSet, subAssessments);
        }
    }

    @Override
    public void undo() {
        int newQuantity = numberToRemove + assessmentSet.getQuantity();
        addSubAssessmentsCommand = new AddSubAssessmentsSubCommand(mainController, assessmentSet, newQuantity);
        addSubAssessmentsCommand.execute();
    }

    @Override
    public void redo() {
        addSubAssessmentsCommand.undo();
    }

    private ButtonType displayAlertAndReturnResponse(int numberToRemove) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/gradebook/dialog-pane.css").toExternalForm());

        alert.setTitle("Reduce Assessment Set Quantity");
        alert.setHeaderText("Are you sure you want to reduce the number of assessments for assessment set \"" + assessmentSet.getName() + "\" by " + numberToRemove + "?");

        if (numberToRemove > 1) {
            alert.setContentText("Reducing the number of assessments in a set by " + numberToRemove + " will permanently remove the last "
                    + numberToRemove + " assessments from the set. Any existing grades will be lost (even if the action is undone with the undo button).");
        } else {
            alert.setContentText("Reducing the number of assessments in a set by " + numberToRemove +
                    " will permanently remove the last assessment from the set. Any existing grades will be lost (even if the action is undone with the undo button).");
        }

        alert.showAndWait();
        return alert.getResult();
    }
}
