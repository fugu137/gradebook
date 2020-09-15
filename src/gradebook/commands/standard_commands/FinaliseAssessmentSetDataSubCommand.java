package gradebook.commands.standard_commands;

import gradebook.model.AssessmentSetData;
import gradebook.model.StdAssessmentData;

import java.util.ArrayList;
import java.util.List;

public class FinaliseAssessmentSetDataSubCommand implements StandardCommand {

    private AssessmentSetData data;
    private List<StandardCommand> subAssessmentFinaliseCommands;

    FinaliseAssessmentSetDataSubCommand(AssessmentSetData data) {
        this.data = data;
        this.subAssessmentFinaliseCommands = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (StdAssessmentData stdData : data.getStdAssessmentDataList()) {
            FinaliseStdAssessmentDataSubCommand command = new FinaliseStdAssessmentDataSubCommand(stdData);
            command.execute();
            subAssessmentFinaliseCommands.add(command);
        }
    }

    @Override
    public void undo() {
        for (StandardCommand c : subAssessmentFinaliseCommands) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        subAssessmentFinaliseCommands.clear();
        execute();
    }
}
