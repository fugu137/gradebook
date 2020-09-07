package gradebook.commands;

import gradebook.model.AssessmentSetData;
import gradebook.model.StdAssessmentData;

import java.util.ArrayList;
import java.util.List;

public class FinaliseAssessmentSetDataCommand implements UserCommand {

    AssessmentSetData data;
    List<UserCommand> subAssessmentFinaliseCommands;

    public FinaliseAssessmentSetDataCommand(AssessmentSetData data) {
        this.data = data;
        this.subAssessmentFinaliseCommands = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (StdAssessmentData stdData : data.getStdAssessmentDataList()) {
            FinaliseStdAssessmentDataCommand command = new FinaliseStdAssessmentDataCommand(stdData);
            command.execute();
            subAssessmentFinaliseCommands.add(command);
        }
    }

    @Override
    public void undo() {
        for (UserCommand c : subAssessmentFinaliseCommands) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        subAssessmentFinaliseCommands.clear();
        execute();
    }
}
