package gradebook.model;

import javafx.beans.property.ObjectProperty;
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
    ObjectProperty<Double> totalGradeMedianProperty();
    ObjectProperty<Double> totalGradeAverageProperty();
    ObjectProperty<Double> totalGradeHighestProperty();
    ObjectProperty<Double> totalGradeLowestProperty();
    ObjectProperty<Double> assessmentMedianProperty(Assessment assessment);
    ObjectProperty<Double> assessmentAverageProperty(Assessment assessment);
    ObjectProperty<Double> assessmentHighestProperty(Assessment assessment);
    ObjectProperty<Double> assessmentLowestProperty(Assessment assessment);


}
