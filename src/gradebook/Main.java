package gradebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("new-gradebook.fxml"));
        primaryStage.setTitle("Gradebook");
        primaryStage.getIcons().add(new Image("\\resources\\icons\\favicon4.png"));

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        primaryStage.setScene(new Scene(root, width/1.6, height/1.5));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
