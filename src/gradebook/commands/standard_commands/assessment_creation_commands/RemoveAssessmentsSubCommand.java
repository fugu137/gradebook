package gradebook.commands.standard_commands.assessment_creation_commands;

import gradebook.MainController;
import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Assessment;
import gradebook.model.Student;
import gradebook.tools.CourseManager;
import javafx.collections.ObservableList;

public class RemoveAssessmentsSubCommand implements StandardCommand {

    private MainController mainController;
    private CourseManager courseManager;
    private Student blankStudent;
    private ObservableList<Assessment> assessmentsToRemove;

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
