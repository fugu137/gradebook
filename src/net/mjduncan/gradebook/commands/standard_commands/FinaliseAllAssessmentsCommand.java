package net.mjduncan.gradebook.commands.standard_commands;

import javafx.collections.ObservableList;
import net.mjduncan.gradebook.model.AssessmentSetData;
import net.mjduncan.gradebook.model.StdAssessmentData;
import net.mjduncan.gradebook.model.Student;

import java.util.ArrayList;
import java.util.List;

public class FinaliseAllAssessmentsCommand implements StandardCommand {

    private ObservableList<Student> students;
    private List<StandardCommand> commands;

    public FinaliseAllAssessmentsCommand(ObservableList<Student> students) {
        this.students = students;
        this.commands = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (Student s : students) {
            s.getAssessmentDataList().forEach(d -> {
                if (d instanceof StdAssessmentData) {
                    StandardCommand command = new FinaliseStdAssessmentDataSubCommand((StdAssessmentData) d);
                    command.execute();
                    commands.add(command);
                }
                if (d instanceof AssessmentSetData) {
                    StandardCommand command = new FinaliseAssessmentSetDataSubCommand((AssessmentSetData) d);
                    command.execute();
                    commands.add(command);
                }
            });
        }
    }

    @Override
    public void undo() {
        for (StandardCommand c : commands) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        commands.forEach(StandardCommand::execute);
    }
}
