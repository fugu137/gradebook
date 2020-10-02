package net.mjduncan.gradebook.tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.mjduncan.gradebook.model.Assessment;
import net.mjduncan.gradebook.model.AssessmentSet;
import net.mjduncan.gradebook.model.StdAssessment;
import net.mjduncan.gradebook.model.Student;

import java.util.List;

public abstract class StudentCloner {

    public static ObservableList<Student> run(ObservableList<Student> studentList) {
        ObservableList<Student> cloneList = FXCollections.observableArrayList();

        for (Student s : studentList) {
            Student studentCopy = new Student(s.getSurname(), s.getGivenNames(), s.getPreferredName(), s.getClassGroup(), s.getGender(), s.getSid(), s.getDegree(), s.getEmail());

            List<Assessment> assessments = s.getAssessmentList();
            copyAssessments(studentCopy, assessments);
            copyGrades(studentCopy, s, assessments);

            cloneList.add(studentCopy);
        }

        return cloneList;
    }

    private static void copyAssessments(Student student, List<Assessment> assessments) {

        assessments.forEach(assessment -> {
            if (assessment instanceof StdAssessment) {
                StdAssessment std = (StdAssessment) assessment;
                student.addStdAssessmentData(std);

            } else if (assessment instanceof AssessmentSet) {
                AssessmentSet set = (AssessmentSet) assessment;
                student.addAssessmentSetData(set);
            }
        });
    }

    private static void copyGrades(Student studentCopy, Student student, List<Assessment> assessments) {

        assessments.forEach(assessment -> {
            if (assessment instanceof StdAssessment) {
                StdAssessment std = (StdAssessment) assessment;
                Integer grade = student.stdAssessmentGradeProperty(std).getValue();

                studentCopy.setStdAssessmentGrade(std, grade);

            } else if (assessment instanceof AssessmentSet) {
                AssessmentSet set = (AssessmentSet) assessment;

                set.getStdAssessments().forEach(subAssessment -> {
                    Integer grade = student.assessmentSetGradeProperty(set, subAssessment).getValue();

                    studentCopy.setSubAssessmentGrade(set, subAssessment, grade);
                });
            }

        });
    }

}
