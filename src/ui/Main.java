package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * The main function responsible of initiating the program.
     * @param args Used to launch the program.
     */
    public static void main(String[] args) {
        launch();
    }
    /**
     * The start function which initializes the stage and displays it. Also deactivates the threads when the window is closed by using an anonymous nested class.
     * @param stage The main stage of the graphical interface.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Machine");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
