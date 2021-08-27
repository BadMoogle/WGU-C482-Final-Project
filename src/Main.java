import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the application
 * @author Daniel Harper
 */
public class Main extends Application {
    public static Inventory inventory;

    /**
     * Starts the application
     * @param primaryStage Primary stage for the application
     * @throws Exception Throws exception if unable to load fxml document
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        inventory = new Inventory();
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 934, 435));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    /**
     * Main entrypoint for the application
     * @param args Arguments to pass in from the command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}
