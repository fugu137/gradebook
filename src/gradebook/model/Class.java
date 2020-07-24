package gradebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Class {

    private StringProperty name;
    private ObservableList<Student> students;

    public Class(String name) {
        this.name = new SimpleStringProperty(name);
        this.students = FXCollections.observableArrayList();
    }

    public String getName() {
        return name.getValue();
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }



    @Override
    public String toString() {
        return name.getValue();
    }
}
