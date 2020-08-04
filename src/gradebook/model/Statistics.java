package gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {

    private ObservableList<Student> classStudents;

    private ObservableList<Student> hdStudents;
    private ObservableList<Student> dStudents;
    private ObservableList<Student> crStudents;
    private ObservableList<Student> pStudents;
    private ObservableList<Student> fStudents;

    private ObjectProperty<Double> median;
    private ObjectProperty<Double> average;
    private ObjectProperty<Double> highestGrade;
    private ObjectProperty<Double> lowestGrade;


    public Statistics(ObservableList<Student> classStudents) {
        this.classStudents = classStudents;

        this.hdStudents = FXCollections.observableArrayList();
        this.dStudents = FXCollections.observableArrayList();
        this.crStudents = FXCollections.observableArrayList();
        this.pStudents = FXCollections.observableArrayList();
        this.fStudents = FXCollections.observableArrayList();

        this.median = new SimpleObjectProperty<>(null);
        this.average = new SimpleObjectProperty<>(null);
        this.highestGrade = new SimpleObjectProperty<>(null);
        this.lowestGrade = new SimpleObjectProperty<>(null);
    }

    public void update(Student student, ObjectProperty<?> gradeProperty) {
        updateGradeLists(student, gradeProperty);
        updateStatistics();
    }

    private void remove(Student student) {
        hdStudents.remove(student);
        dStudents.remove(student);
        crStudents.remove(student);
        pStudents.remove(student);
        fStudents.remove(student);
    }

    private void updateGradeLists(Student student, ObjectProperty<?> gradeProperty) {
        remove(student);

        if (gradeProperty.getValue() != null) {
            double grade = Double.parseDouble(String.valueOf(gradeProperty.getValue()));

            if (grade < 50) {
                fStudents.add(student);

            } else if (grade < 65) {
                pStudents.add(student);

            } else if (grade < 75) {
                crStudents.add(student);

            } else if (grade < 85) {
                dStudents.add(student);

            } else if (grade <= 100) {
                hdStudents.add(student);

            } else {
                System.out.println("Unable to update statistics. Invalid grade.");
            }
        }
    }

    void updateStatistics() {
        List<Double> grades = new ArrayList<>();

        for (Student s : classStudents) {
            if (s.getTotalGrade() != null) {
                grades.add(s.getTotalGrade());
            }
        }
        Collections.sort(grades);

        this.lowestGrade.set(grades.get(0));
        this.highestGrade.set(grades.get(grades.size() - 1));

        setMedian(grades);
        setAverage(grades);

    }

    void setMedian(List<Double> grades) {
        double median;

        if (grades.size() % 2 == 0) {
            int middleIndex = (grades.size() / 2) - 1;
            median = (grades.get(middleIndex) + grades.get(middleIndex + 1)) / 2;

        } else {
            int middleIndex = grades.size() / 2;
            median = grades.get(middleIndex);
        }

        this.median.set(median);
    }

    void setAverage(List<Double> grades) {
        double sum = 0;
        int number = 0;

        for (Double d : grades) {
            sum = sum + d;
            number++;
        }
        this.average.set(sum / number);
    }

    public int numberOfHDs() {
        return hdStudents.size();
    }

    public int numberOfDs() {
        return dStudents.size();
    }

    public int numberOfCRs() {
        return crStudents.size();
    }

    public int numberOfPs() {
        return pStudents.size();
    }

    public int numberOfFs() {
        return fStudents.size();
    }

    public int numberOfStudents() {
        return hdStudents.size() + dStudents.size() + crStudents.size() + pStudents.size() + fStudents.size();
    }

    public ObservableList<Student> getHDStudents() {
        return hdStudents;
    }

    public ObservableList<Student> getDStudents() {
        return dStudents;
    }

    public ObservableList<Student> getCRStudents() {
        return crStudents;
    }

    public ObservableList<Student> getPStudents() {
        return pStudents;
    }

    public ObservableList<Student> getFStudents() {
        return fStudents;
    }

    public Double getMedian() {
        return median.get();
    }

    public ObjectProperty<Double> medianProperty() {
        return median;
    }

    public Double getAverage() {
        return average.get();
    }

    public ObjectProperty<Double> averageProperty() {
        return average;
    }

    public Double getHighestGrade() {
        return highestGrade.get();
    }

    public ObjectProperty<Double> highestGradeProperty() {
        return highestGrade;
    }

    public Double getLowestGrade() {
        return lowestGrade.get();
    }

    public ObjectProperty<Double> lowestGradeProperty() {
        return lowestGrade;
    }

    public ObservableList<Student> getClassStudents() {
        return classStudents;
    }
}
