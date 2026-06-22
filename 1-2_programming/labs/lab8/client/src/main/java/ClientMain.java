import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientMain extends Application {

    public static io.net.Client networkClient;

    public static void main(String[] args) {
        try {
            io.ui.gui.controllers.AuthController.networkClient =
                    new io.net.Client(InetAddress.getLocalHost(), 48496);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale currentLocale = new Locale("ru", "RU");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.gui", currentLocale);

        URL fxmlLocation = getClass().getResource("/views/AuthView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("Error");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setResources(bundle);

        Parent root = loader.load();

        primaryStage.setTitle(bundle.getString("auth.title"));
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}