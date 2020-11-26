package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import net.mjduncan.gradebook.tools.NumberRounder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {

    private final ObservableList<Student> classStudents;

    private final ObservableList<Student> hdStudents;
    private final ObservableList<Student> dStudents;
    private final ObservableList<Student> crStudents;
    private final ObservableList<Student> pStudents;
    private final ObservableList<Student> fStudents;

    private final ObjectProperty<Double> median;
    private final ObjectProperty<Double> average;
    private final ObjectProperty<Double> highestGrade;
    private final ObjectProperty<Double> lowestGrade;

    private final ObjectProperty<Integer> numberOfStudentsWithGrades;

    private BarChart<String, Number> barChart;
    private XYChart.Data<String, Number> hdData;
    private XYChart.Data<String, Number> dData;
    private XYChart.Data<String, Number> crData;
    private XYChart.Data<String, Number> pData;
    private XYChart.Data<String, Number> fData;


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

        this.numberOfStudentsWithGrades = new SimpleObjectProperty<>(null);

    }

    public void update(Student student, ObjectProperty<?> gradeProperty) {
        updateStatistics();
        updateGradeLists(student, gradeProperty);
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
        this.numberOfStudentsWithGrades.set(grades.size());

        Collections.sort(grades);

        if (grades.size() > 0) {
            this.lowestGrade.set(grades.get(0));
            this.highestGrade.set(grades.get(grades.size() - 1));

        } else {
            this.numberOfStudentsWithGrades.set(null);
            this.lowestGrade.set(null);
            this.highestGrade.set(null);
        }

        setMedian(grades);
        setAverage(grades);

    }

    void setMedian(List<Double> grades) {

        if (grades.size() == 0) {
            this.median.set(null);

        } else {
            double median;

            if (grades.size() % 2 == 0) {
                int middleIndex = (grades.size() / 2) - 1;
                median = (grades.get(middleIndex) + grades.get(middleIndex + 1)) / 2;
                median = NumberRounder.round(median, 1);

            } else {
                int middleIndex = grades.size() / 2;
                median = grades.get(middleIndex);
            }

            this.median.set(median);
        }
    }

    void setAverage(List<Double> grades) {

        if (grades.size() == 0) {
            this.average.set(null);

        } else {
            double sum = 0;
            int number = 0;

            for (Double d : grades) {
                sum = sum + d;
                number++;
            }
            this.average.set(NumberRounder.round(sum / number, 1));
        }
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

    public Integer getNumberOfStudentsWithGrades() {
        return numberOfStudentsWithGrades.getValue();
    }

    public ObjectProperty<Integer> numberOfStudentsWithGradesProperty() {
        return numberOfStudentsWithGrades;
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

    public Double getPercentageOfHDs() {
        if (numberOfHDs() > 0) {
            return NumberRounder.round((double) numberOfHDs() * 100 / getNumberOfStudentsWithGrades(), 1);
        } else {
            return 0.0;
        }
    }

    public Double getPercentageOfDs() {
        if (numberOfDs() > 0) {
            return NumberRounder.round((double) numberOfDs() * 100 / getNumberOfStudentsWithGrades(), 1);
        } else {
            return 0.0;
        }
    }

    public Double getPercentageOfCRs() {
        if (numberOfCRs() > 0) {
            return NumberRounder.round((double) numberOfCRs() * 100 / getNumberOfStudentsWithGrades(), 1);
        } else {
            return 0.0;
        }
    }

    public Double getPercentageOfPs() {
        if (numberOfPs() > 0) {
            return NumberRounder.round((double) numberOfPs() * 100 / getNumberOfStudentsWithGrades(), 1);
        } else {
            return 0.0;
        }
    }

    public Double getPercentageOfFs() {
        if (numberOfFs() > 0) {
            return NumberRounder.round((double) numberOfFs() * 100 / getNumberOfStudentsWithGrades(), 1);
        } else {
            return 0.0;
        }
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

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    public void setBarChart(BarChart<String, Number> barChart) {
        this.barChart = barChart;
    }

    void fillBarChart(BarChart<String, Number> barChart, StudentGroup group) {
        this.barChart = barChart;
        barChart.setTitle("Total Grade");
        addChartData(barChart, group);
    }

    void addChartData(BarChart<String, Number> barChart, StudentGroup group) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(group.getName());

        hdData = new XYChart.Data<>("HD", getPercentageOfHDs());
        series.getData().add(hdData);

        dData = new XYChart.Data<>("D", getPercentageOfDs());
        series.getData().add(dData);

        crData = new XYChart.Data<>("CR", getPercentageOfCRs());
        series.getData().add(crData);

        pData = new XYChart.Data<>("P", getPercentageOfPs());
        series.getData().add(pData);

        fData = new XYChart.Data<>("F", getPercentageOfFs());
        series.getData().add(fData);

        barChart.getData().add(series);
    }

    public void updateBarChartValues() {
        hdData.setYValue(getPercentageOfHDs());
        dData.setYValue(getPercentageOfDs());
        crData.setYValue(getPercentageOfCRs());
        pData.setYValue(getPercentageOfPs());
        fData.setYValue(getPercentageOfFs());
    }
}
