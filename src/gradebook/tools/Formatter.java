package gradebook.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public abstract class Formatter {

    public static void formatWeightingBox(TextField field) {
        ChangeListener<String> formatter = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s1, String s2) {

                if (s2 != null) {
                    try {
                        int i = Integer.parseInt(s2);
                        if (i < 0 || i > 100) {
                            field.setStyle("-fx-text-fill: red");
                        } else if (i == 100) {
                            field.setStyle("-fx-text-fill: green");
                        } else {
                            field.setStyle(null);
                        }
                    } catch (Exception e) {
                        System.out.println("Cannot parse as int " + e.getMessage());
                    }
                }
            }
        };
        field.textProperty().addListener(formatter);
    }
}