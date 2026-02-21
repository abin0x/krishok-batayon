package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {

    // --- FXML Controls ---
    // Sidebar Buttons
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, btnLabor;

    // Search Controls
    @FXML private TextField searchField;
    @FXML private Button searchBtn, loadMoreBtn;

    // Container (Changed from GridPane to FlowPane to match your FXML)
    @FXML private FlowPane warehouseContainer;

    // --- Data ---
    private List<StorageFacility> allFacilities = new ArrayList<>();
    private List<StorageFacility> filteredFacilities = new ArrayList<>();
    private int displayCount = 6; // How many to show initially

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Navigation
        // Ensure these buttons exist in your FXML or pass null if they don't
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // 2. Initialize Logic
        loadData();
        setupActions();

        // 3. Render initial view
        renderGrid();
    }

    // ---------------------------------------------------------
    // 1. DATA LOADING
    // ---------------------------------------------------------
    private void loadData() {
        allFacilities.clear();
        allFacilities.addAll(Arrays.asList(
                new StorageFacility("à¦•à§ƒà¦·à¦¿ à¦­à¦¾à¦¨à§à¦¡à¦¾à¦° à¦“ à¦—à§à¦¦à¦¾à¦®", "à¦—à§à¦¦à¦¾à¦®", "à¦¨à¦°à¦¸à¦¿à¦‚à¦¦à§€, à¦¶à¦¿à¦¬à¦ªà§à¦°", "à¦®à§‹à¦ƒ à¦¸à¦¾à¦²à¦¾à¦® à¦‰à¦¦à§à¦¦à¦¿à¦¨", "à§«à§¦à§¦", "à§©à§¨à§¦", "à§®à§«à§¦", 64, new String[]{"à¦§à¦¾à¦¨", "à¦—à¦®", "+à§§"}, new String[]{"à¦“à¦œà¦¨ à¦®à¦¾à¦ªà¦¾"}),
                new StorageFacility("à¦†à¦§à§à¦¨à¦¿à¦• à¦¶à§€à¦¤à¦¾à¦¤à¦ª à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¬à¦—à§à¦¡à¦¼à¦¾, à¦¶à§‡à¦°à¦ªà§à¦°", "à¦†à¦¬à§ à¦¤à¦¾à¦¹à§‡à¦°", "à§§à§¦à§¦à§¦", "à§¬à§«à§¦", "à§®à§¦à§¦", 65, new String[]{"à¦†à¦²à§", "à¦ªà§‡à¦à¦¯à¦¼à¦¾à¦œ", "+à§¨"}, new String[]{"à¦¤à¦¾à¦ªà¦®à¦¾à¦¤à§à¦°à¦¾ à¦¨à¦¿à¦¯à¦¼à¦¨à§à¦¤à§à¦°à¦£"}),
                new StorageFacility("à¦°à¦¹à¦®à¦¾à¦¨ à¦•à§ƒà¦·à¦¿ à¦—à§à¦¦à¦¾à¦®", "à¦—à§à¦¦à¦¾à¦®", "à¦•à§à¦®à¦¿à¦²à§à¦²à¦¾, à¦šà§Œà¦¦à§à¦¦à¦—à§à¦°à¦¾à¦®", "à¦®à§‹à¦ƒ à¦°à¦¹à¦®à¦¾à¦¨", "à§«à§¦à§¦", "à§¨à§¦à§¦", "à§§à§«à§¦", 40, new String[]{"à¦§à¦¾à¦¨", "à¦­à§à¦Ÿà§à¦Ÿà¦¾", "+à§§"}, new String[]{"à¦²à§‹à¦¡à¦¿à¦‚-à¦†à¦¨à¦²à§‹à¦¡à¦¿à¦‚"}),
                new StorageFacility("à¦¸à¦¬à§à¦œ à¦•à§ƒà¦·à¦¿ à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¦à¦¿à¦¨à¦¾à¦œà¦ªà§à¦°, à¦¬à¦¿à¦°à¦¾à¦®à¦ªà§à¦°", "à¦®à§‹. à¦œà¦¾à¦¹à¦¾à¦™à§à¦—à§€à¦°", "à§®à§¦à§¦", "à§ªà§®à§¦", "à§©à§«à§¦", 72, new String[]{"à¦†à¦²à§", "à¦Ÿà¦®à§‡à¦Ÿà§‹", "+à§¨"}, new String[]{"à¦•à§‹à¦²à§à¦¡ à¦šà§‡à¦‡à¦¨"}),
                new StorageFacility("à¦®à¦¡à¦¾à¦°à§à¦¨ à¦à¦—à§à¦°à§‹ à¦¸à§à¦Ÿà§‹à¦°à§‡à¦œ", "à¦—à§à¦¦à¦¾à¦®", "à¦°à¦¾à¦œà¦¶à¦¾à¦¹à§€, à¦—à§‹à¦¦à¦¾à¦—à¦¾à¦¡à¦¼à§€", "à¦†à¦¬à§à¦¦à§à¦² à¦•à¦°à¦¿à¦®", "à§¬à§¦à§¦", "à§ªà§¨à§¦", "à§¨à§¨à§¦", 58, new String[]{"à¦§à¦¾à¦¨", "à¦¸à¦°à¦¿à¦·à¦¾", "+à§§"}, new String[]{"à¦¶à§à¦·à§à¦•à§€à¦•à¦°à¦£ à¦¯à¦¨à§à¦¤à§à¦°"}),
                new StorageFacility("à¦šà¦Ÿà§à¦Ÿà¦—à§à¦°à¦¾à¦® à¦¸à§‡à¦¨à§à¦Ÿà§à¦°à¦¾à¦² à¦¸à§à¦Ÿà§‹à¦°à§‡à¦œ", "à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦šà¦Ÿà§à¦Ÿà¦—à§à¦°à¦¾à¦®, à¦¹à¦¾à¦Ÿà¦¹à¦¾à¦œà¦¾à¦°à§€", "à¦®à§‹. à¦¶à¦«à¦¿à¦•à§à¦²", "à§§à§¨à§¦à§¦", "à§®à§«à§¦", "à§ªà§«à§¦", 68, new String[]{"à¦†à¦²à§", "à¦°à¦¸à§à¦¨", "+à§©"}, new String[]{"à¦…à¦Ÿà§‹ à¦¤à¦¾à¦ªà¦®à¦¾à¦¤à§à¦°à¦¾"}),
                new StorageFacility("à¦†à¦§à§à¦¨à¦¿à¦• à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¹à¦¿à¦®à¦¾à¦—à¦¾à¦°", "à¦¨à¦°à¦¸à¦¿à¦‚à¦¦à§€ à¦¸à¦¦à¦°", "à¦‡à¦žà§à¦œà¦¿. à¦¸à¦¾à¦²à¦®à¦¾à¦¨ à¦šà§Œà¦§à§à¦°à§€", "à§¨à§¦à§¦", "à§®à§¦", "à§©à§¦à§¦", 40, new String[]{"à¦†à¦²à§", "à¦Ÿà¦®à§‡à¦Ÿà§‹", "+à§§"}, new String[]{"à¦¤à¦¾à¦ªà¦®à¦¾à¦¤à§à¦°à¦¾ à¦¨à¦¿à¦¯à¦¼à¦¨à§à¦¤à§à¦°à¦£"}),
                new StorageFacility("à¦•à§ƒà¦·à¦• à¦¸à¦®à¦¬à¦¾à§Ÿ à¦—à§à¦¦à¦¾à¦®", "à¦—à§à¦¦à¦¾à¦®", "à¦®à§à¦¨à§à¦¸à¦¿à¦—à¦žà§à¦œ, à¦—à¦œà¦¾à¦°à¦¿à¦¯à¦¼à¦¾", "à¦®à§‹. à¦¨à¦œà¦°à§à¦² à¦‡à¦¸à¦²à¦¾à¦®", "à§©à§¦à§¦", "à§¦", "à§§à§¨à§¦", 0, new String[]{"à¦§à¦¾à¦¨", "à¦†à¦²à§"}, new String[]{"à¦•à§€à¦Ÿà¦¨à¦¾à¦¶à¦• à¦¸à§à¦ªà§à¦°à§‡"})
        ));
        filteredFacilities.addAll(allFacilities);
    }

    // ---------------------------------------------------------
    // 2. SEARCH ACTIONS
    // ---------------------------------------------------------
    private void setupActions() {
        // Filter when search button clicked
        if (searchBtn != null) searchBtn.setOnAction(e -> filterData());

        // Filter as you type (Real-time searching)
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterData());
        }

        // Load More button
        if (loadMoreBtn != null) loadMoreBtn.setOnAction(e -> {
            displayCount += 3;
            renderGrid();
        });
    }

    private void filterData() {
        String query = (searchField != null) ? searchField.getText().toLowerCase().trim() : "";

        filteredFacilities = allFacilities.stream()
                .filter(f -> query.isEmpty() || f.matches(query))
                .collect(Collectors.toList());

        // Reset display count or keep it? Let's reset to show results clearly
        // displayCount = filteredFacilities.size();
        renderGrid();
    }

    // ---------------------------------------------------------
    // 3. UI RENDERING
    // ---------------------------------------------------------
    private void renderGrid() {
        if (warehouseContainer == null) return;

        // Clear existing cards (removes hardcoded FXML cards and shows Java generated ones)
        warehouseContainer.getChildren().clear();

        int limit = Math.min(displayCount, filteredFacilities.size());

        for (int i = 0; i < limit; i++) {
            // Add card to FlowPane
            warehouseContainer.getChildren().add(createCard(filteredFacilities.get(i)));
        }

        // Manage Load More Button visibility
        if (loadMoreBtn != null) {
            boolean hasMore = limit < filteredFacilities.size();
            loadMoreBtn.setVisible(hasMore);
            loadMoreBtn.setManaged(hasMore);
        }
    }

    private VBox createCard(StorageFacility f) {
        VBox card = new VBox(15);
        card.getStyleClass().add("warehouse-card");

        // Header
        Label title = new Label(f.title);
        title.getStyleClass().add("card-title");
        title.setWrapText(true);
        title.setMaxWidth(180);

        // Badge Logic
        Label badge;
        if (f.vacancyRate == 0) {
            badge = new Label("ðŸ•’ à¦ªà§‚à¦°à§à¦£");
            badge.setStyle("-fx-background-color: #ef4444;"); // Red for full
        } else {
            badge = new Label("âœ” " + f.vacancyRate + "% à¦«à¦¾à¦à¦•à¦¾");
        }
        badge.getStyleClass().add("badge-black");

        HBox header = new HBox(10, title, new Region(), badge);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        // Type Tag
        Label typeTag = new Label(f.type);
        typeTag.getStyleClass().add("tag-grey");

        // Details
        VBox details = new VBox(10,
                createRow("ðŸ“", f.location),
                createRow("ðŸ‘¤", f.owner),
                createRow("ðŸ“¦", f.available + "/" + f.capacity + " à¦Ÿà¦¨"),
                createRow("ðŸ’°", "à§³ " + f.price + "/à¦Ÿà¦¨")
        );
        details.getStyleClass().add("details-box");

        // Crops Tags
        HBox crops = new HBox(5);
        if(f.crops != null) Arrays.stream(f.crops).forEach(c -> crops.getChildren().add(createTag(c, "tag-white")));

        // Facilities Tags
        HBox facilities = new HBox(5);
        if(f.facilities != null) Arrays.stream(f.facilities).forEach(fac -> facilities.getChildren().add(createTag(fac, "tag-facility")));

        // Contact Button
        Button contactBtn = new Button("ðŸ“ž à¦¯à§‹à¦—à¦¾à¦¯à§‹à¦— à¦•à¦°à§à¦¨");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);

        if (f.vacancyRate == 0) {
            contactBtn.setDisable(true);
            contactBtn.setStyle("-fx-background-color: #9ca3af; -fx-cursor: default;");
        } else {
            contactBtn.setOnAction(e -> showAlert(f));
        }

        card.getChildren().addAll(header, typeTag, details, new Label("à¦«à¦¸à¦²:"), crops, new Label("à¦¸à§à¦¬à¦¿à¦§à¦¾:"), facilities, contactBtn);
        return card;
    }

    private HBox createRow(String icon, String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("detail-text");
        return new HBox(10, new Label(icon), lbl);
    }

    private Label createTag(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }

    private void showAlert(StorageFacility f) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact Information");
        alert.setHeaderText(f.title);
        alert.setContentText("Owner: " + f.owner + "\nLocation: " + f.location + "\nCall: 01712-000000");
        alert.show();
    }

    // ---------------------------------------------------------
    // INTERNAL MODEL CLASS
    // ---------------------------------------------------------
    private static class StorageFacility {
        String title, type, location, owner, capacity, available, price;
        int vacancyRate;
        String[] crops, facilities;

        StorageFacility(String t, String ty, String l, String o, String c, String a, String p, int v, String[] cr, String[] f) {
            title = t; type = ty; location = l; owner = o; capacity = c; available = a; price = p; vacancyRate = v; crops = cr; facilities = f;
        }

        // Search logic
        boolean matches(String query) {
            return title.toLowerCase().contains(query) ||
                    location.toLowerCase().contains(query) ||
                    owner.toLowerCase().contains(query) ||
                    type.toLowerCase().contains(query);
        }
    }
}


