package gradebook;

import gradebook.model.CourseManager;
import gradebook.model.Statistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class StatisticsPane extends HBox {

    VBox statsPanel;
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

//        fillBarChart(courseManager);
    }

    private void addStatsPanel() {
        this.setPadding(new Insets(10, 8, 0, 0));
        this.statsPanel = new VBox();
        this.getChildren().add(statsPanel);
    }

    public void addCharts() {
        VBox chartBox = new VBox();
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


    public void addBarChart(CourseManager courseManager) {
//        if (courseManager.getClass("R11A") != null) {
//            this.barChart = courseManager.getClass("R11A").createOverallGradeBarChart();
//        }
//        if (barChart != null) {
//            statsPanel.getChildren().add(barChart);
//        }

        this.xAxis = new CategoryAxis();
        xAxis.setLabel("Grades");

        this.yAxis = new NumberAxis();
        yAxis.setLabel("Percentage of students");

        this.barChart = new BarChart<String, Number>(xAxis, yAxis);
        statsPanel.getChildren().clear();

        VBox box = new VBox();
        box.setAlignment(Pos.TOP_RIGHT);
        box.getChildren().add(barChart);

        Statistics stats = courseManager.getCourseCohort().getTotalStatistics();

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("HD (" + stats.getPercentageOfHDs() + "%)", stats.numberOfHDs()),
                        new PieChart.Data("D (" + stats.getPercentageOfDs() + "%)", stats.numberOfDs()),
                        new PieChart.Data("CR (" + stats.getPercentageOfCRs() + "%)", stats.numberOfCRs()),
                        new PieChart.Data("P (" + stats.getPercentageOfPs() + "%)", stats.numberOfPs()),
                        new PieChart.Data("F (" + stats.getPercentageOfFs() + "%)", stats.numberOfFs()));
        pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Total Grade (All Classes)");
        pieChart.setPrefHeight(320);


        box.getChildren().add(pieChart);

        HBox footer = new HBox();
        footer.setAlignment(Pos.BOTTOM_CENTER);
        closeButton = new Button("Close");
        closeButton.getStyleClass().add("chart-button");
        footer.getChildren().add(closeButton);

        statsPanel.getChildren().add(box);
        statsPanel.getChildren().add(footer);

        box.prefHeightProperty().bind(statsPanel.heightProperty());
        footer.setPrefHeight(10);

    }

    private void setStyles() {
        this.getStyleClass().add("white-pane");
        statsPanel.getStyleClass().add("bordered-white-pane");
        statsPanel.setPrefWidth(370);
        statsPanel.setAlignment(Pos.TOP_CENTER);
        statsPanel.setSpacing(30);
        statsPanel.setPadding(new Insets(0, -5, 10, -5));
    }

    public void fillBarChart(CourseManager courseManager) {

        courseManager.fillBarChartWithOverallGrades(barChart);

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

    public void fillPieChart(CourseManager courseManager) {
        courseManager.fillPieChart(pieChart);
    }

    public BarChart<String, Number> getChart() {
        return barChart;
    }

}
