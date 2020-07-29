package gradebook.tools;

import gradebook.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class StudentCloner {

    public static ObservableList<Student> run(ObservableList<Student> studentList) {
        ObservableList<Student> cloneList = FXCollections.observableArrayList();

        for (Student s : studentList) {
            cloneList.add(new Student(s.getSurname(), s.getGivenNames(), s.getPreferredName(), s.getClassGroup(), s.getGender(), s.getSid(), s.getDegree(), s.getEmail()));
        }
        //TODO: clone grades
        return cloneList;
    }
}
