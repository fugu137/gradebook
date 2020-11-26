package net.mjduncan.gradebook.commands.standard_commands.assessment_creation_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Assessment;
import net.mjduncan.gradebook.model.AssessmentCreationBar;

public class AddBarAssessmentSubCommand implements StandardCommand {

    private final AssessmentCreationBar bar;
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
