package com.example.demo1.app.controller;

import com.example.demo1.app.controller.SidebarController;
import com.example.demo1.app.controller.TopbarController;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SceneLoader;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    private static final String DEFAULT_VIEW = "/fxml/dashboard/dashboard.fxml";

    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentArea;

    @FXML
    private TopbarController topbarIncludeController;

    private SidebarController sidebarController;
    private String activeViewPath;
    private boolean loadingView;
    private static MainLayoutController instance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        if (topbarIncludeController != null) {
            topbarIncludeController.setMainLayoutController(this);
        }
        loadSidebarComponent();
        loadView(DEFAULT_VIEW);
    }

    public static MainLayoutController getInstance() {
        return instance;
    }

    public void handleLogout() {
        SessionManager.clearSession();
        try {
            NavigationHelper.navigateTo(contentArea.getScene(), "/fxml/features/hello-view.fxml");
        } catch (Exception e) {
            UiAlerts.errorWithCause("Logout Error", "Could not return to login page.", e);
        }
    }

    public boolean loadView(String fxmlPath) {
        if (fxmlPath == null || fxmlPath.isBlank()) {
            return false;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> loadView(fxmlPath));
            return true;
        }

        if (loadingView || fxmlPath.equals(activeViewPath)) {
            return false;
        }

        loadingView = true;
        try {
            Parent view = SceneLoader.load(fxmlPath);
            Node contentNode = extractContentNode(view);
            contentArea.getChildren().setAll(contentNode);
            activeViewPath = fxmlPath;

            if (sidebarController != null) {
                sidebarController.syncActiveView(activeViewPath);
            }
            return true;
        } catch (Exception e) {
            showError("Could not load view: " + fxmlPath, e);
            return false;
        } finally {
            loadingView = false;
        }
    }

    public String getActiveViewPath() {
        return activeViewPath;
    }

    private Node extractContentNode(Parent loadedRoot) {
        if (loadedRoot instanceof BorderPane borderPane && borderPane.getCenter() != null) {
            Node centerNode = borderPane.getCenter();
            borderPane.setCenter(null);
            if (centerNode instanceof Parent centerParent) {
                centerParent.getStylesheets().addAll(loadedRoot.getStylesheets());
                centerParent.getStyleClass().addAll(loadedRoot.getStyleClass());
                if (loadedRoot.getStyle() != null && !loadedRoot.getStyle().isBlank()) {
                    centerParent.setStyle(loadedRoot.getStyle());
                }
            }
            return centerNode;
        }
        return loadedRoot;
    }

    private void loadSidebarComponent() {
        String sidebarPath = "/fxml/sidebar/sidebar.fxml";
        try {
            URL sidebarUrl = getClass().getResource(sidebarPath);
            if (sidebarUrl == null) {
                showError("Sidebar resource not found: " + sidebarPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(sidebarUrl);
            Node sidebar = loader.load();
            sidebarController = loader.getController();
            if (sidebarController == null) {
                showError("Sidebar controller is null for: " + sidebarPath);
                return;
            }
            sidebarController.setMainLayoutController(this);
            sidebarContainer.getChildren().setAll(sidebar);
        } catch (Exception e) {
            showError("Could not load sidebar component: " + sidebarPath, e);
        }
    }

    private void showError(String message) {
        UiAlerts.error("Navigation Error", message);
    }

    private void showError(String message, Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        UiAlerts.errorWithCause("Navigation Error", message, root);
        throwable.printStackTrace();
    }
}
