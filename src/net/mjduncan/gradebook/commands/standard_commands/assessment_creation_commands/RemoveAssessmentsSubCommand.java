package net.mjduncan.gradebook.commands.standard_commands.assessment_creation_commands;

import javafx.collections.ObservableList;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Assessment;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;

public class RemoveAssessmentsSubCommand implements StandardCommand {

    private final MainController mainController;
    private final CourseManager courseManager;
    private final Student blankStudent;
    private final ObservableList<Assessment> assessmentsToRemove;

    RemoveAssessmentsSubCommand(MainController mainController, ObservableList<Assessment> assessmentsToRemove) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.blankStudent = mainController.getBlankStudent();
        this.assessmentsToRemove = assessmentsToRemove;
    }

    @Override
    public void execute() {
        assessmentsToRemove.forEach(assessment -> {
            courseManager.unassignAssessment(assessment);
            blankStudent.removeAssessmentData(assessment);
            mainController.removeAssessmentColumn(assessment);
        });

        if (courseManager.getAssessments().size() < 1) {
            mainController.removeTotalColumn();
        }
    }

    @Override
    public void undo() {
        assessmentsToRemove.forEach(assessment -> {
            new AddAssessmentSubCommand(mainController, assessment).execute();
        });

        mainController.createTotalColumn();
    }

    @Override
    public void redo() {
        execute();
    }

}
