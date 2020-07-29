package gradebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CourseManager {

    private StringProperty courseName;
    private ObservableList<Student> allStudents;
    private ObservableList<Class> classes;
    private Class unassigned;
    private ObservableList<Assessment> assessments;

    public CourseManager(String courseName) {
        this.courseName = new SimpleStringProperty(courseName);
        this.allStudents = FXCollections.observableArrayList();
        this.classes = FXCollections.observableArrayList();
        this.unassigned = new Class("None");
        classes.add(unassigned);
        this.assessments = FXCollections.observableArrayList();
    }

    public void setCourseName(String name) {
        this.courseName.set(name);
    }

    public void newStudent(Student student) {

        Class studentClass = student.getClassGroup();
        ObservableList<Assessment> assessments;

        if (studentClass == null) {
            student.setClassGroup(unassigned);
            unassigned.addStudent(student);
            allStudents.add(student);

            assessments = unassigned.getAssessments();

        } else {
            if (!classes.contains(studentClass)) {
                classes.add(studentClass);
                addAllAssessments(studentClass);
            }
            studentClass.addStudent(student);
            allStudents.add(student);

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
    }

    public void newStudents(ObservableList<Student> students) {
        students.forEach(this::newStudent);
    }

    public void reAddAllStudentsAt(int index, ObservableList<Student> students) {
        allStudents.addAll(index, students);
        for (Student s: students) {
            Class classGroup = s.getClassGroup();
            classGroup.addStudent(s);
        }
    }

    public void removeStudent(Student student) {
        allStudents.remove(student);
        for (Class c: classes) {
            c.removeStudent(student);
        }
    }

    public ObservableList<Student> getAllStudents() {
        return allStudents;
    }

    public void addClass(Class classGroup) {

        System.out.println("Class list: " + classes.toString());

        if (classes.contains(classGroup)) {
            System.out.println("Class already exists!");

        } else {
            classes.add(classGroup);
            System.out.println("Adding " + classGroup.getName());
        }
    }

    public Class getClass(String className) {
        Class targetClass = null;

        for (Class c: classes) {
            if (c.getName().equals(className)) {
                targetClass = c;
                break;
            }
        }
        return targetClass;
    }

    public ObservableList<Class> getClasses() {
        return classes;
    }

    public Class getUnassigned() {
        return unassigned;
    }

    public void addAllAssessments(Class classGroup) {
        assessments.forEach(classGroup::addAssessment);
    }

    public void assignAssessment(Assessment assessment) {
        assessments.add(assessment);

        for (Class c: classes) {
            c.addAssessment(assessment);
        }
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    public String getCourseName() {
        return courseName.getValue();
    }

}
