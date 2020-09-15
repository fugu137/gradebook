package gradebook.commands.standard_commands.assessment_creation_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Assessment;
import gradebook.model.AssessmentCreationBar;

public class AddBarAssessmentSubCommand implements StandardCommand {

    private AssessmentCreationBar bar;
    private Assessment assessment;

    AddBarAssessmentSubCommand(AssessmentCreationBar bar) {
        this.bar = bar;
    }

    @Override
    public void execute() {
        bar.createAssessment();
        this.assessment = bar.getAssessment();
    }

    @Override
    public void undo() {
        bar.removeAssessment();
    }

    @Override
    public void redo() {
        bar.addAssessment(assessment);
    }
}
