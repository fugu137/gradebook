package gradebook;

import gradebook.model.AssessmentColumn;
import gradebook.model.CourseManager;
import gradebook.model.Student;
import gradebook.model.StudentGroup;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class StatisticsPane extends HBox {

    VBox statsPanel;
    VBox chartBox;
    Button closeButton;

    BarChart<String, Number> barChart;
    CategoryAxis xAxis;
    NumberAxis yAxis;

    PieChart pieChart;

    public StatisticsPane() {
        addStatsPanel();
        setStyles();
        addCharts();
        addFooter();
    }

    private void addStatsPanel() {
        this.setPadding(new Insets(10, 8, 0, 0));
        this.statsPanel = new VBox();
        this.getChildren().add(statsPanel);
    }

    public void addCharts() {
        chartBox = new VBox();
        chartBox.setAlignment(Pos.TOP_RIGHT);

        this.xAxis = new CategoryAxis();
        this.yAxis = new NumberAxis();

        xAxis.setLabel("Grades");
        yAxis.setLabel("Percentage of Students");

        barChart = new BarChart<String, Number>(xAxis, yAxis);
        pieChart = new PieChart();

        chartBox.getChildren().add(barChart);
        chartBox.getChildren().add(pieChart);

        statsPanel.getChildren().add(chartBox);
    }

    private void addFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.BOTTOM_CENTER);
        footer.setPrefHeight(10);

        closeButton = new Button("Close");
        closeButton.getStyleClass().add("chart-button");
        footer.getChildren().add(closeButton);

        statsPanel.getChildren().add(footer);
    }

    private void setStyles() {
        this.getStyleClass().add("white-pane");
        statsPanel.getStyleClass().add("bordered-white-pane");
        statsPanel.setPrefWidth(370);
        statsPanel.setAlignment(Pos.TOP_CENTER);
        statsPanel.setSpacing(30);
        statsPanel.setPadding(new Insets(0, -5, 12, -5));
    }

    public void fillBarChart(CourseManager courseManager, AssessmentColumn<Student, ?> totalColumn, ComboBox<StudentGroup> classComboBox, ComboBox<AssessmentColumn<Student, ?>> columnComboBox) {
        StudentGroup selectedGroup = classComboBox.getSelectionModel().getSelectedItem();
        AssessmentColumn<Student, ?> selectedColumn = columnComboBox.getSelectionModel().getSelectedItem();

        System.out.println("Filling bar chart...");
        if (selectedGroup != null && selectedColumn != null) {

            if (selectedGroup == courseManager.getCourseCohort()) {
                if (selectedColumn == totalColumn) {
                    courseManager.fillBarChartWithOverallGrades(barChart);

                } else {
                    if (selectedColumn.getAssessment() != null) {
                        courseManager.fillBarChartWithAssessmentGrades(barChart, selectedColumn.getAssessment());
                    } else {
                        System.out.println("No assessment found for column " + selectedColumn.getText());
                    }
                }
            } else {
                if (selectedColumn == totalColumn) {
                    selectedGroup.fillBarChartWithOverallGrades(barChart);

                } else {
                    if (selectedColumn.getAssessment() != null) {
                        selectedGroup.fillBarChartWithAssessmentGrades(barChart, selectedColumn.getAssessment());
                    } else {
                        System.out.println("No assessment found for column " + selectedColumn.getText());
                    }
                }
            }


            addTooltips();

        } else {
            System.out.println("Class or column not selected [statistics tab]");
        }


//        courseManager.fillBarChartWithOverallGrades(barChart);

//        if (courseManager.getClass("R11A") != null) {
//            Assessment assessment = courseManager.getClass("R11A").getAssessments().get(0);
//            courseManager.getClass("R11A").fillBarChartWithAssessmentGrades(barChart, assessment);
//        }

//        if (courseManager.getAssessments().size() > 0) {
//            Assessment assessment = courseManager.getAssessments().get(0);
//            courseManager.fillBarChartWithAssessmentGrades(barChart, assessment);
//        }


//        barChart.setTitle("Overall Grade Distribution");
//
//        ObservableList<StudentGroup> studentGroups = FXCollections.observableArrayList();
//        studentGroups.addAll(groups);
//        studentGroups.removeIf(s -> s.getStudents().size() < 1);
//
//        barChart.getData().clear();
//        if (courseManager.getStudentGroups() != null) {
//
//            for (StudentGroup c : courseManager.getStudentGroups()) {
//                XYChart.Series<String, Number> series = new XYChart.Series<>();
//                series.setName(c.getName());
//
//                series.getData().add(new XYChart.Data<>("HD", c.getTotalStatistics().getPercentageOfHDs()));
//                series.getData().add(new XYChart.Data<>("D", c.getTotalStatistics().getPercentageOfDs()));
//                series.getData().add(new XYChart.Data<>("CR", c.getTotalStatistics().getPercentageOfCRs()));
//                series.getData().add(new XYChart.Data<>("P", c.getTotalStatistics().getPercentageOfPs()));
//                series.getData().add(new XYChart.Data<>("F", c.getTotalStatistics().getPercentageOfFs()));
//
//                barChart.getData().add(series);
//            }
//        }

    }

    private void addTooltips() {

        for (XYChart.Series<String, Number> s : barChart.getData()) {
            for (XYChart.Data<String, Number> data : s.getData()) {
                StringProperty textProperty = new SimpleStringProperty();
                textProperty.bind(data.YValueProperty().asString());

                Tooltip tooltip = new Tooltip();
                tooltip.textProperty().bind(textProperty);

                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }

    public void fillPieChart(CourseManager courseManager) {
        courseManager.fillPieChart(pieChart);
    }

    public void replaceAndFillBarChart(CourseManager courseManager, AssessmentColumn<Student, ?> totalColumn, ComboBox<StudentGroup> classComboBox, ComboBox<AssessmentColumn<Student, ?>> columnComboBox) {
        chartBox.getChildren().remove(barChart);

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        chartBox.getChildren().add(0, barChart);

        fillBarChart(courseManager, totalColumn, classComboBox, columnComboBox);
    }

    public Button getCloseButton() {
        return closeButton;
    }

}
