package gradebook.model;

import javafx.collections.ObservableList;

public interface StudentGroup {

    String getName();
    ObservableList<Student> getStudents();
    Statistics getStatistics(Assessment assessment);
    Statistics getTotalStatistics();
    ObservableList<Assessment> getAssessments();
    int getNumberAttempted(Assessment assessment);
    int getNumberOfStudents();
    void addAssessment(Assessment assessment);


}
