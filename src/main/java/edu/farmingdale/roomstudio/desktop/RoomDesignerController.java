package edu.farmingdale.roomstudio.desktop;

import java.util.function.Consumer;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class RoomDesignerController {
    @FXML private WebView designer;
    @FXML private Label status;

    private String address;
    private Consumer<String> openExternal;

    void connect(int port, Consumer<String> openExternal) {
        this.address = "http://127.0.0.1:" + port + "/";
        this.openExternal = openExternal;

        WebEngine engine = designer.getEngine();
        engine.getLoadWorker().stateProperty().addListener((observable, previous, next) -> {
            if (next == Worker.State.SUCCEEDED) {
                status.setText("Designer ready");
            } else if (next == Worker.State.FAILED) {
                status.setText("Unable to load the designer. Try Open in browser.");
            } else if (next == Worker.State.RUNNING) {
                status.setText("Loading designer…");
            }
        });

        // Links to retailer and contractor sites open in the system browser.
        engine.setCreatePopupHandler(features -> {
            WebEngine popup = new WebEngine();
            popup.locationProperty().addListener((observable, previous, url) -> {
                if (url.startsWith("https://") || url.startsWith("http://")) {
                    this.openExternal.accept(url);
                    popup.getLoadWorker().cancel();
                }
            });
            return popup;
        });
        engine.load(address);
    }

    @FXML
    private void reloadDesigner() {
        designer.getEngine().load(address);
    }

    @FXML
    private void openInBrowser() {
        openExternal.accept(address);
    }
}
