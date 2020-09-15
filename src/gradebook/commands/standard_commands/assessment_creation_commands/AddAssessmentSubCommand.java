package gradebook.commands.standard_commands.assessment_creation_commands;

import gradebook.MainController;
import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Assessment;
import gradebook.model.AssessmentSet;
import gradebook.model.StdAssessment;
import gradebook.model.Student;
import gradebook.tools.CourseManager;

public class AddAssessmentSubCommand implements StandardCommand {

    private MainController mainController;
    private Assessment assessment;
    private CourseManager courseManager;
    private Student blankStudent;

    public AddAssessmentSubCommand(MainController mainController, Assessment assessment) {
        this.mainController = mainController;
        this.assessment = assessment;
        this.courseManager = mainController.getCourseManager();
        this.blankStudent = mainController.getBlankStudent();
    }

    @Override
    public void execute() {

        if (assessment instanceof StdAssessment) {
            StdAssessment std = (StdAssessment) assessment;

            courseManager.assignAssessment(std);
            blankStudent.addStdAssessmentData(std);
            mainController.createStdAssessmentColumn(std);
        }

        if (assessment instanceof AssessmentSet) {
            AssessmentSet set = (AssessmentSet) assessment;

            courseManager.assignAssessment(set);
            blankStudent.addAssessmentSetData(set);
            mainController.createAssessmentSetColumns(set);
        }
    }

    @Override
    public void undo() {
        if (assessment instanceof StdAssessment) {
            StdAssessment std = (StdAssessment) assessment;

            courseManager.unassignAssessment(std); //TODO: check
            blankStudent.removeAssessmentData(std);
            mainController.removeStdAssessmentColumn(std);
        }

        if (assessment instanceof AssessmentSet) {
            AssessmentSet set = (AssessmentSet) assessment;

            courseManager.unassignAssessment(set); //TODO: check
            blankStudent.removeAssessmentData(set);
            mainController.removeAssessmentSetColumns(set);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
