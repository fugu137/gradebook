package gradebook.commands;

import gradebook.model.StdAssessmentData;

public class FinaliseStdAssessmentDataCommand implements UserCommand {

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
