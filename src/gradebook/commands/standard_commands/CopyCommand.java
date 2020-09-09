package gradebook.commands.standard_commands;

import gradebook.MainController;
import gradebook.model.Student;
import gradebook.tools.StudentCloner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CopyCommand implements StandardCommand {

    private MainController mainController;
    private TableView<Student> table;
    private ObservableList<Student> clipBoardCopy;

    public CopyCommand(MainController mainController) {
        this.mainController = mainController;
        this.table = mainController.getTable();
        this.clipBoardCopy = FXCollections.observableArrayList();

        clipBoardCopy = mainController.getClipBoardStudents();
    }

    @Override
    public void execute() {
        mainController.getClipBoardStudents().clear();
        ObservableList<Student> toCopy = table.getSelectionModel().getSelectedItems();
        ObservableList<Student> copied = StudentCloner.run(toCopy);

        mainController.getClipBoardStudents().addAll(copied);
    }

    @Override
    public void undo() {
        mainController.getClipBoardStudents().clear();
        mainController.getClipBoardStudents().addAll(clipBoardCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}
