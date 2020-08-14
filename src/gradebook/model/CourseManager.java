package gradebook.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;


public class CourseManager {

    private CourseCohort courseCohort;
    private ObservableList<StudentGroup> studentGroups;
    private ObservableList<Class> classes;
    private Class unassigned;
    private ObservableList<Assessment> assessments;

    public CourseManager(String courseName) {
        this.courseCohort = new CourseCohort(courseName);
        this.unassigned = new Class("None");
        this.studentGroups = FXCollections.observableArrayList();
        this.classes = FXCollections.observableArrayList();
        studentGroups.add(courseCohort);
        studentGroups.add(unassigned);
        classes.add(unassigned);
        this.assessments = FXCollections.observableArrayList();
    }

    public void clear() {
        courseCohort.clear();
        unassigned.clear();
        studentGroups.removeIf(s -> !(s == courseCohort) && !(s == unassigned));
        classes.removeIf(s -> !(s == unassigned));
        assessments.clear();
    }

    public void setCourseName(String name) {
        this.courseCohort.setName(name);
    }

    public void newStudent(Student student) {

        Class studentClass = student.getClassGroup();
        ObservableList<Assessment> assessments;

        student.setCourseCohort(courseCohort);

        if (studentClass == null) {
            student.setClassGroup(unassigned);
            unassigned.addStudent(student);
            courseCohort.addStudent(student);

            assessments = unassigned.getAssessments();

        } else {
            if (!classes.contains(studentClass)) {
                classes.add(studentClass);
                studentGroups.add(studentClass);
                addAllAssessments(studentClass);
            }
            studentClass.addStudent(student);
            courseCohort.addStudent(student);

            assessments = studentClass.getAssessments();
        }

//        assessments.stream().filter(a -> a instanceof StdAssessment).map(a -> (StdAssessment) a).forEach(student::addStdAssessmentData);
//        assessments.stream().filter(a -> a instanceof AssessmentSet).map(a -> (AssessmentSet) a).forEach(student::addAssessmentSetData);

        for (Assessment a : assessments) {

            if (a instanceof StdAssessment) {
                student.addStdAssessmentData((StdAssessment) a);
            } else if (a instanceof AssessmentSet) {
                student.addAssessmentSetData((AssessmentSet) a);
            } else {
                System.out.println("Assessment type not found...");
            }
        }

        addClassListener(student);
    }

    private void addClassListener(Student student) {
        student.classGroupProperty().addListener((obs, oldValue, newValue) -> {
            oldValue.removeStudent(student);
            newValue.addStudent(student);
        });
    }

    public void newStudents(ObservableList<Student> students) {
        students.forEach(this::newStudent);
    }

    public void reAddAllStudentsAt(int index, ObservableList<Student> students) {
        courseCohort.addAllAt(index, students);

        for (Student s : students) {

            if (s.getAssessmentDataList().size() > 0) {
                Class classGroup = s.getClassGroup();
                classGroup.addStudent(s);

            } else {
                newStudent(s);
            }
        }
    }

    public void removeStudent(Student student) {
        courseCohort.removeStudent(student);
        for (Class c : classes) {
            c.removeStudent(student);
        }
    }

    public ObservableList<Student> getAllStudents() {
        return courseCohort.getStudents();
    }

    public void addClass(Class classGroup) {

        if (classes.contains(classGroup)) {
            System.out.println("Class already exists!");

        } else {
            classes.add(classGroup);
            studentGroups.add(classGroup);

            for (Assessment a: assessments) {
                classGroup.addAssessment(a);
            }
        }
    }

    public Class getClass(String className) {
        Class targetClass = null;

        for (Class c : classes) {
            if (c.getName().equals(className)) {
                targetClass = c;
                break;
            }
        }
        return targetClass;
    }

    public StudentGroup getStudentGroup(String groupName) {
        StudentGroup targetGroup = null;

        for (StudentGroup g : studentGroups) {
            if (g.getName().equals(groupName)) {
                targetGroup = g;
                break;
            }
        }
        return targetGroup;
    }

    public ObservableList<Class> getClasses() {
        return classes;
    }


    public CourseCohort getCourseCohort() {
        return courseCohort;
    }

    public Class getUnassigned() {
        return unassigned;
    }

    public void addAllAssessments(Class classGroup) {
        assessments.forEach(classGroup::addAssessment);
    }

    public void assignAssessment(Assessment assessment) {
        assessments.add(assessment);

        for (StudentGroup g : studentGroups) {
            g.addAssessment(assessment);
        }
    }

    public void unassignAssessment(Assessment assessment) {
        assessments.remove(assessment);

        for (Class c : classes) {
            c.removeAssessment(assessment);
        }
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    public String getCourseName() {
        return getCourseCohort().getName();
    }

    public ObservableList<StudentGroup> getStudentGroups() {
        return studentGroups;
    }

    public void fillBarChartWithOverallGrades(BarChart<String, Number> barChart) {
        barChart.setTitle("Total Grade");
        addOverallGradeChartData(barChart);

    }


    void addOverallGradeChartData(BarChart<String, Number> barChart) {
        ObservableList<StudentGroup> groups = FXCollections.observableArrayList();
        groups.addAll(studentGroups);
        groups.removeIf(s -> s.getStudents().size() < 1);

        studentGroups.addListener(new ListChangeListener<StudentGroup>() {
            @Override
            public void onChanged(Change<? extends StudentGroup> change) {
                while (change.next()) {
                    System.out.println("Student group added or removed!");

                    List<StudentGroup> newClasses = new ArrayList<>(change.getAddedSubList());
                    addChartDataByGroup(barChart, newClasses);
                }
            }
        });

        addChartDataByGroup(barChart, groups);
    }

    //Helper Method for addOverallGradeChartData()
    private void addChartDataByGroup(BarChart<String, Number> barChart, List<StudentGroup> groups) {
        for (StudentGroup c : groups) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(c.toString());

            XYChart.Data<String, Number> hdData = new XYChart.Data<>("HD", c.getTotalStatistics().getPercentageOfHDs());
            series.getData().add(hdData);
            c.getTotalStatistics().getHDStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getTotalStatistics().getPercentageOfHDs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> dData = new XYChart.Data<>("D", c.getTotalStatistics().getPercentageOfDs());
            series.getData().add(dData);
            c.getTotalStatistics().getDStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            dData.setYValue(c.getTotalStatistics().getPercentageOfDs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> crData = new XYChart.Data<>("CR", c.getTotalStatistics().getPercentageOfCRs());
            series.getData().add(crData);
            c.getTotalStatistics().getCRStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            crData.setYValue(c.getTotalStatistics().getPercentageOfCRs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> pData = new XYChart.Data<>("P", c.getTotalStatistics().getPercentageOfPs());
            series.getData().add(pData);
            c.getTotalStatistics().getPStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            pData.setYValue(c.getTotalStatistics().getPercentageOfPs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> fData = new XYChart.Data<>("F", c.getTotalStatistics().getPercentageOfFs());
            series.getData().add(fData);
            c.getTotalStatistics().getFStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            fData.setYValue(c.getTotalStatistics().getPercentageOfFs());
                        }
                    }
                }
            });

            barChart.getData().add(series);
        }
    }

    //TODO: replace above with this
    private void addTotalGradeListener(ObservableList<Student> studentsWithGrade, XYChart.Data<String, Number> data, double percentage) {
        studentsWithGrade.addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        data.setYValue(percentage);
                    }
                }
            }
        });
    }


    public void fillBarChartWithAssessmentGrades(BarChart<String, Number> barChart, Assessment assessment) {
        barChart.setTitle(assessment.columnName());
        addOverallGradeChartData(barChart);
    }

    void addAssessmentChartData(BarChart<String, Number> barChart, Assessment assessment) {
        ObservableList<StudentGroup> groups = FXCollections.observableArrayList();
        groups.addAll(studentGroups);
        groups.removeIf(s -> s.getStudents().size() < 1);

        for (StudentGroup c : groups) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(c.getName());

            XYChart.Data<String, Number> hdData = new XYChart.Data<>("HD", c.getStatistics(assessment).getPercentageOfHDs());
            series.getData().add(hdData);
            c.getStatistics(assessment).getHDStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getStatistics(assessment).getPercentageOfHDs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> dData = new XYChart.Data<>("D", c.getStatistics(assessment).getPercentageOfDs());
            series.getData().add(dData);
            c.getStatistics(assessment).getDStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getStatistics(assessment).getPercentageOfDs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> crData = new XYChart.Data<>("CR", c.getStatistics(assessment).getPercentageOfCRs());
            series.getData().add(crData);
            c.getStatistics(assessment).getCRStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getStatistics(assessment).getPercentageOfCRs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> pData = new XYChart.Data<>("P", c.getStatistics(assessment).getPercentageOfPs());
            series.getData().add(pData);
            c.getStatistics(assessment).getPStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getStatistics(assessment).getPercentageOfPs());
                        }
                    }
                }
            });

            XYChart.Data<String, Number> fData = new XYChart.Data<>("F", c.getStatistics(assessment).getPercentageOfFs());
            series.getData().add(fData);
            c.getStatistics(assessment).getFStudents().addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            hdData.setYValue(c.getStatistics(assessment).getPercentageOfFs());
                        }
                    }
                }
            });

            barChart.getData().add(series);
        }

    }

    public void fillPieChart(PieChart pieChart) {
        Statistics stats = courseCohort.getTotalStatistics();

        PieChart.Data hdData = new PieChart.Data("HD (" + stats.getPercentageOfHDs() + "%)", stats.numberOfHDs());
        PieChart.Data dData = new PieChart.Data("D (" + stats.getPercentageOfDs() + "%)", stats.numberOfDs());
        PieChart.Data crData = new PieChart.Data("CR (" + stats.getPercentageOfCRs() + "%)", stats.numberOfCRs());
        PieChart.Data pData = new PieChart.Data("P (" + stats.getPercentageOfPs() + "%)", stats.numberOfPs());
        PieChart.Data fData = new PieChart.Data("F (" + stats.getPercentageOfFs() + "%)", stats.numberOfFs());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(hdData, dData, crData, pData, fData);

        pieChart.getData().addAll(pieChartData);
        pieChart.setTitle("Total Grade (All Classes)");

        stats.getHDStudents().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        hdData.setName("HD (" + stats.getPercentageOfHDs() + "%)");
                        hdData.setPieValue(stats.numberOfHDs());
                    }
                }
            }
        });

        stats.getDStudents().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        dData.setName("D (" + stats.getPercentageOfDs() + "%)");
                        dData.setPieValue(stats.numberOfDs());
                    }
                }
            }
        });

        stats.getCRStudents().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        crData.setName("CR (" + stats.getPercentageOfCRs() + "%)");
                        crData.setPieValue(stats.numberOfCRs());
                    }
                }
            }
        });

        stats.getPStudents().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        pData.setName("P (" + stats.getPercentageOfPs() + "%)");
                        pData.setPieValue(stats.numberOfPs());
                    }
                }
            }
        });

        stats.getFStudents().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {
                        fData.setName("F (" + stats.getPercentageOfFs() + "%)");
                        fData.setPieValue(stats.numberOfFs());
                    }
                }
            }
        });
    }



}
