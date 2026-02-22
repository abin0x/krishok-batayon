package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MachineryController implements Initializable {

    private static final String CATEGORY_ALL = "সব ক্যাটাগরি";

    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnStorage;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnMachinery;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private FlowPane machineryContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCards());
        }

        if (categoryCombo != null) {
            categoryCombo.setOnAction(e -> filterCards());
        }
    }

    private void filterCards() {
        if (machineryContainer == null) {
            return;
        }

        String searchText = searchField != null && searchField.getText() != null
                ? searchField.getText().toLowerCase().trim()
                : "";
        String selectedCategory = categoryCombo != null && categoryCombo.getValue() != null
                ? categoryCombo.getValue()
                : CATEGORY_ALL;

        for (Node node : machineryContainer.getChildren()) {
            if (!(node instanceof VBox card)) {
                continue;
            }

            String cardContent = extractTextFromNode(card).toLowerCase();
            boolean matchesCategory = CATEGORY_ALL.equals(selectedCategory)
                    || cardContent.contains(selectedCategory.toLowerCase());
            boolean matchesSearch = searchText.isEmpty() || cardContent.contains(searchText);

            boolean visible = matchesCategory && matchesSearch;
            card.setVisible(visible);
            card.setManaged(visible);
        }
    }

    private String extractTextFromNode(Parent node) {
        StringBuilder builder = new StringBuilder();
        for (Node child : node.getChildrenUnmodifiable()) {
            if (child instanceof Label label) {
                builder.append(label.getText()).append(' ');
            } else if (child instanceof Parent parent) {
                builder.append(extractTextFromNode(parent));
            }
        }
        return builder.toString();
    }
}
