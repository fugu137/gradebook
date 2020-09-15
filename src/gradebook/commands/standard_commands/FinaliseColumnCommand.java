package gradebook.commands.standard_commands;

import gradebook.model.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class FinaliseColumnCommand implements StandardCommand {

    private AssessmentColumn<Student, ?> column;
    private Assessment assessment;
    private ObservableList<Student> students;
    private List<StandardCommand> commands;

    public FinaliseColumnCommand(AssessmentColumn<Student, ?> column, ObservableList<Student> students) {
        this.column = column;
        this.assessment = column.getAssessment();
        this.students = students;
        this.commands = new ArrayList<>();
    }

    @Override
    public void execute() {

        if (column instanceof SubAssessmentColumn) {
            SubAssessmentColumn<?, ?> subAssessmentColumn = (SubAssessmentColumn<?, ?>) column;
            AssessmentSet set = subAssessmentColumn.getParent();
            StdAssessment stdAssessment = (StdAssessment) assessment;

            for (Student s : students) {
                StdAssessmentData stdData = s.getSubAssessmentData(set, stdAssessment);
                StandardCommand command = new FinaliseStdAssessmentDataSubCommand(stdData);
                command.execute();
                commands.add(command);
            }

        } else {
            for (Student s : students) {
                AssessmentData d = s.getAssessmentData(assessment);

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
            }
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
