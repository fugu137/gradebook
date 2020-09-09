package gradebook.commands.standard_commands;

import gradebook.model.StdAssessmentData;

public class FinaliseStdAssessmentDataCommand implements StandardCommand {

    private StdAssessmentData data;
    private boolean wasChanged = false;

    public FinaliseStdAssessmentDataCommand(StdAssessmentData data) {
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
