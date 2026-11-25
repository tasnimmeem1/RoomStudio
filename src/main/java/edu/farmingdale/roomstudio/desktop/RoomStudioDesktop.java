package edu.farmingdale.roomstudio.desktop;

import edu.farmingdale.roomstudio.RoomStudioApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class RoomStudioDesktop extends Application {
    private ServletWebServerApplicationContext server;

    @Override
    public void start(Stage stage) {
        try {
            var arguments = getParameters().getRaw().toArray(String[]::new);
            server = (ServletWebServerApplicationContext)
                SpringApplication.run(RoomStudioApplication.class, arguments);
            URL view = Objects.requireNonNull(
                RoomStudioDesktop.class.getResource("/room-designer.fxml"),
                "room-designer.fxml is missing"
            );
            FXMLLoader loader = new FXMLLoader(view);
            Scene scene = new Scene(loader.load(), 1280, 800);
            RoomDesignerController controller = loader.getController();
            controller.connect(server.getWebServer().getPort(), getHostServices()::showDocument);
            stage.setTitle("Room Studio");
            stage.setMinWidth(700);
            stage.setMinHeight(520);
            stage.setScene(scene);
            stage.show();
        } catch (Exception error) {
            stop();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Room Studio could not start");
            alert.setContentText(error.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.close();
            server = null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
