package com.example.demo1.app.controller;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SceneLoader;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.net.URL;

public class MainLayoutController {

    private static final String DEFAULT_VIEW = "/fxml/dashboard/dashboard.fxml";
    @FXML
    private VBox sidebarContainer;
    @FXML
    private StackPane contentArea;
    @FXML
    private TopbarController topbarIncludeController;
    private SidebarController sidebarController;
    private String activeViewPath;//now majkhane kun page load hoche seta track korbe
    private boolean loadingView;// ek sathe jeno 2 bar kuno page load na hoy seta ensure korbe
    private static MainLayoutController instance;//pura class a akta matro copy dhore rakhbe,jate onno jayga theke access kora jai

    @FXML
    private void initialize() {
        instance = this;//nije re save kore rakhe
        if (topbarIncludeController != null) {
            topbarIncludeController.setMainLayoutController(this);//topbar controller ke main layout controller er reference diye deya hocche, jate topbar theke main layout er method gulo call kora jai
        }
        loadSidebarComponent();
        loadView(DEFAULT_VIEW);
    }


    //MainLayoutController er ekta global access point create kora hocche, jate onno controller theke main layout controller er method gulo call kora jai
    public static MainLayoutController getInstance() {
        return instance;
    }

    //Logout method, jeta session clear kore login page e navigate kore
    public void handleLogout() {
        SessionManager.clearSession();
        try {
            NavigationHelper.navigateTo(contentArea.getScene(), "/fxml/features/hello-view.fxml");
        } catch (Exception e) {
            UiAlerts.errorWithCause("Logout Error", "Could not return to login page.", e);
        }
    }


    //View loading method, jeta given FXML path theke view load kore content area te set kore, active view path update kore, and sidebar ke sync kore. Thread safety ensure korar jonno
    public boolean loadView(String fxmlPath) {
        if (fxmlPath == null || fxmlPath.isBlank()) {
            return false;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> loadView(fxmlPath));
            return true;
        }

        if (loadingView || fxmlPath.equals(activeViewPath)) {// jodi ekta view already load hocche thake, ba requested view already active thake, tahole abar load korar proyojon nei
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


    //Sidebar component load method, jeta sidebar.fxml theke sidebar component load kore, controller reference set kore, and sidebar container e add kore. Error handling o ache jate resource na pele ba controller null pele user ke alert deya jai
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
            sidebarController.setMainLayoutController(this);//sidebar controller ke main layout controller er reference diye deya hocche, jate sidebar theke main layout er method gulo call kora jai
            sidebarContainer.getChildren().setAll(sidebar);
        } catch (Exception e) {
            showError("Could not load sidebar component: " + sidebarPath, e);
        }
    }


    //Error handling methods, jeta user ke alert deya jai jodi navigation error hoy, ba view load korte problem hoy. Throwable version ta root cause identify kore alert deya o stack trace print kore
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
