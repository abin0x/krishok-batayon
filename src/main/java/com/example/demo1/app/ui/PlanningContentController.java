package com.example.demo1.app.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlanningContentController {

    private static final int INITIAL_VISIBLE_CARDS = 8;

    @FXML
    private GridPane cropsGrid;

    @FXML
    private Button seeMoreButton;

    private final List<Node> cropCards = new ArrayList<>();
    private boolean expanded;

    @FXML
    private void initialize() {
        cropCards.clear();
        for (Node child : cropsGrid.getChildren()) {
            if (child instanceof VBox) {
                cropCards.add(child);
            }
        }

        cropCards.sort(Comparator
                .comparingInt((Node node) -> GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node))
                .thenComparingInt((Node node) -> GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node)));

        if (cropCards.size() <= INITIAL_VISIBLE_CARDS) {
            seeMoreButton.setVisible(false);
            seeMoreButton.setManaged(false);
            return;
        }

        seeMoreButton.setVisible(true);
        seeMoreButton.setManaged(true);
        setExpanded(false);
    }

    @FXML
    private void toggleSeeMore() {
        setExpanded(!expanded);
    }

    private void setExpanded(boolean showAll) {
        expanded = showAll;
        for (int i = 0; i < cropCards.size(); i++) {
            boolean visible = showAll || i < INITIAL_VISIBLE_CARDS;
            Node card = cropCards.get(i);
            card.setVisible(visible);
            card.setManaged(visible);
        }
        seeMoreButton.setText(showAll ? "Show Less" : "Show More");
    }
}
