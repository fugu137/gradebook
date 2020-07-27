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

    public CourseManager(String courseName) {
        this.courseName = new SimpleStringProperty(courseName);
        this.allStudents = FXCollections.observableArrayList();
        this.classes = FXCollections.observableArrayList();
        this.unassigned = new Class("None");
        classes.add(unassigned);

    }

    public void newStudent(Student student) {
        Class studentClass = student.getClassGroup();

        if (studentClass == null) {
            student.setClassGroup(unassigned);
            unassigned.addStudent(student);
            allStudents.add(student);

        } else {
            classes.add(studentClass);
            studentClass.addStudent(student);
            allStudents.add(student);
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

    public void assignAssessment(Assessment assessment) {
        for (Class c: classes) {
            c.addAssessment(assessment);
        }
    }

}
