package gradebook.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CourseManager {

    private Course cohort;
    private ObservableList<StudentGroup> studentGroups;
    private ObservableList<Class> classes;
    private Class unassigned;
    private ObservableList<Assessment> assessments;

    public CourseManager(String courseName) {
        this.cohort = new Course(courseName);
        this.unassigned = new Class("None");
        this.studentGroups = FXCollections.observableArrayList();
        this.classes = FXCollections.observableArrayList();
        studentGroups.add(cohort);
        studentGroups.add(unassigned);
        classes.add(unassigned);
        this.assessments = FXCollections.observableArrayList();
    }

    public void setCourseName(String name) {
        this.cohort.setName(name);
    }

    public void newStudent(Student student) {

        Class studentClass = student.getClassGroup();
        ObservableList<Assessment> assessments;

        student.setCourse(cohort);

        if (studentClass == null) {
            student.setClassGroup(unassigned);
            unassigned.addStudent(student);
            cohort.addStudent(student);

            assessments = unassigned.getAssessments();

        } else {
            if (!classes.contains(studentClass)) {
                classes.add(studentClass);
                studentGroups.add(studentClass);
                addAllAssessments(studentClass);
            }
            studentClass.addStudent(student);
            cohort.addStudent(student);

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
        cohort.addAllAt(index, students);
        for (Student s: students) {
            Class classGroup = s.getClassGroup();
            classGroup.addStudent(s);
        }
    }

    public void removeStudent(Student student) {
        cohort.removeStudent(student);
        for (Class c: classes) {
            c.removeStudent(student);
        }
    }

    public ObservableList<Student> getAllStudents() {
        return cohort.getStudents();
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

    public StudentGroup getStudentGroup(String groupName) {
        StudentGroup targetGroup = null;

        for (StudentGroup g: studentGroups) {
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


    public Course getCohort() {
        return cohort;
    }

    public Class getUnassigned() {
        return unassigned;
    }

    public void addAllAssessments(Class classGroup) {
        assessments.forEach(classGroup::addAssessment);
    }

    public void assignAssessment(Assessment assessment) {
        assessments.add(assessment);

        for (StudentGroup g: studentGroups) {
            g.addAssessment(assessment);
        }
    }

    public void unassignAssessment(Assessment assessment) {
        assessments.remove(assessment);

        for (Class c: classes) {
            c.removeAssessment(assessment);
        }
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    public String getCourseName() {
        return getCohort().getName();
    }

    public ObservableList<StudentGroup> getStudentGroups() {
        return studentGroups;
    }

}
