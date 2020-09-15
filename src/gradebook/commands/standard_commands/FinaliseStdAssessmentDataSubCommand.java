package gradebook.commands.standard_commands;

import gradebook.model.StdAssessmentData;

public class FinaliseStdAssessmentDataSubCommand implements StandardCommand {

    private StdAssessmentData data;
    private boolean wasChanged = false;

    FinaliseStdAssessmentDataSubCommand(StdAssessmentData data) {
        this.data = data;
    }

    @Override
    public void execute() {
        wasChanged = data.setIncompleteToZero();
    }

    @Override
    public void undo() {
        if (wasChanged) {
            data.setGrade(null);
        }
    }

    @Override
    public void redo() {
        wasChanged = false;
        execute();
    }
}
