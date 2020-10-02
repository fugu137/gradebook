package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.AssessmentSet;
import net.mjduncan.gradebook.model.StdAssessment;
import net.mjduncan.gradebook.model.Student;

public class ChangeGradeCommand implements StandardCommand {

    private AssessmentSet assessmentSet;
    private StdAssessment stdAssessment;
    private Student student;
    private Integer oldGrade;
    private Integer newGrade;

    public ChangeGradeCommand(StdAssessment stdAssessment, Student student, Integer oldGrade, Integer newGrade) {
        this(null, stdAssessment, student, oldGrade, newGrade);
    }

    public ChangeGradeCommand(AssessmentSet assessmentSet, StdAssessment stdAssessment, Student student, Integer oldGrade, Integer newGrade) {
        this.assessmentSet = assessmentSet;
        this.stdAssessment = stdAssessment;
        this.student = student;
        this.oldGrade = oldGrade;
        this.newGrade = newGrade;
    }

    @Override
    public void execute() {
        if (assessmentSet == null) {
//            student.stdAssessmentGradeProperty(stdAssessment).set(newGrade);
            student.setStdAssessmentGrade(stdAssessment, newGrade);

        } else {
//            student.assessmentSetGradeProperty(assessmentSet, stdAssessment).set(newGrade);
            student.setSubAssessmentGrade(assessmentSet, stdAssessment, newGrade);
        }
    }

    @Override
    public void undo() {
        if (assessmentSet == null) {
            student.stdAssessmentGradeProperty(stdAssessment).set(oldGrade);

        } else {
            student.assessmentSetGradeProperty(assessmentSet, stdAssessment).set(oldGrade);
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
