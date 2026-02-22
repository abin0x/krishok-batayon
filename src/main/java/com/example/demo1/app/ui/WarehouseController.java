package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {

    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnStorage;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnMachinery;

    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button loadMoreBtn;
    @FXML private ComboBox<String> districtComboBox;
    @FXML private Label lblKpiAvailableValue;
    @FXML private Label lblKpiAvailableTrend;
    @FXML private Label lblKpiOccupancyValue;
    @FXML private Label lblKpiOccupancyTrend;
    @FXML private Label lblKpiPriceValue;
    @FXML private Label lblKpiPriceTrend;
    @FXML private Label lblKpiVerifiedValue;
    @FXML private Label lblKpiVerifiedTrend;

    @FXML private GridPane warehouseContainer;

    private final List<StorageFacility> allFacilities = new ArrayList<>();
    private List<StorageFacility> filteredFacilities = new ArrayList<>();
    private int displayCount = 6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        loadData();
        setupDistrictFilter();
        setupActions();
        setupGridLayout();

        renderGrid();
    }

    private void setupGridLayout() {
        if (warehouseContainer == null) {
            return;
        }
        warehouseContainer.getColumnConstraints().clear();
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.3333);
            warehouseContainer.getColumnConstraints().add(col);
        }
        warehouseContainer.setHgap(18);
        warehouseContainer.setVgap(18);
    }

    private void setupDistrictFilter() {
        if (districtComboBox == null) {
            return;
        }

        Set<String> districts = new LinkedHashSet<>();
        districts.add("সব জেলা");
        allFacilities.forEach(f -> districts.add(f.district));

        districtComboBox.getItems().setAll(districts);
        districtComboBox.setValue("সব জেলা");
        districtComboBox.setOnAction(e -> filterData());
    }

    private void loadData() {
        allFacilities.clear();
        allFacilities.addAll(Arrays.asList(
                new StorageFacility("কৃষি ভান্ডার ও গুদাম", "গুদাম", "নরসিংদী", "শিবপুর", "মোঃ সালাম উদ্দিন", "500", "320", "850", 64, "+8801819684174", new String[]{"ধান", "গম", "+১"}, new String[]{"ওজন মাপা"}),
                new StorageFacility("আধুনিক শীতাতপ হিমাগার", "হিমাগার", "বগুড়া", "শেরপুর", "আবু তাহের", "1000", "650", "800", 65, "+8801819684174", new String[]{"আলু", "পেঁয়াজ", "+২"}, new String[]{"তাপমাত্রা নিয়ন্ত্রণ"}),
                new StorageFacility("রহমান কৃষি গুদাম", "গুদাম", "কুমিল্লা", "চৌদ্দগ্রাম", "মোঃ রহমান", "500", "200", "150", 40, "+8801819684174", new String[]{"ধান", "ভুট্টা", "+১"}, new String[]{"লোডিং-আনলোডিং"}),
                new StorageFacility("সবুজ কৃষি হিমাগার", "হিমাগার", "দিনাজপুর", "বিরামপুর", "মো. জাহাঙ্গীর", "800", "480", "350", 72, "+8801819684174", new String[]{"আলু", "টমেটো", "+২"}, new String[]{"কোল্ড চেইন"}),
                new StorageFacility("মডার্ন এগ্রো স্টোরেজ", "গুদাম", "রাজশাহী", "গোদাগাড়ী", "আব্দুল করিম", "600", "420", "220", 58, "+8801819684174", new String[]{"ধান", "সরিষা", "+১"}, new String[]{"শুষ্কীকরণ যন্ত্র"}),
                new StorageFacility("চট্টগ্রাম সেন্ট্রাল স্টোরেজ", "হিমাগার", "চট্টগ্রাম", "হাটহাজারী", "মো. শফিকুল", "1200", "850", "450", 68, "+8801819684174", new String[]{"আলু", "রসুন", "+৩"}, new String[]{"অটো তাপমাত্রা"}),
                new StorageFacility("আধুনিক হিমাগার", "হিমাগার", "নরসিংদী", "সদর", "ইঞ্জি. সালমান চৌধুরী", "200", "80", "300", 40, "+8801819684174", new String[]{"আলু", "টমেটো", "+১"}, new String[]{"তাপমাত্রা নিয়ন্ত্রণ"}),
                new StorageFacility("কৃষক সমবায় গুদাম", "গুদাম", "মুন্সিগঞ্জ", "গজারিয়া", "মো. নজরুল ইসলাম", "300", "0", "120", 0, "+8801819684174", new String[]{"ধান", "আলু"}, new String[]{"কীটনাশক স্প্রে"}),
                new StorageFacility("গ্রিনফিল্ড স্টোরেজ", "গুদাম", "যশোর", "অভয়নগর", "মো. ইমন", "450", "180", "170", 40, "+8801819684174", new String[]{"ধান", "গম"}, new String[]{"ডিজিটাল ওজন"})
        ));
        filteredFacilities = new ArrayList<>(allFacilities);
    }

    private void setupActions() {
        if (searchBtn != null) {
            searchBtn.setOnAction(e -> filterData());
        }

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterData());
        }

        if (loadMoreBtn != null) {
            loadMoreBtn.setOnAction(e -> {
                displayCount += 3;
                renderGrid();
            });
        }
    }

    private void filterData() {
        String query = searchField == null ? "" : searchField.getText().toLowerCase().trim();
        String districtFilter = districtComboBox == null ? "সব জেলা" : districtComboBox.getValue();

        filteredFacilities = allFacilities.stream()
                .filter(f -> query.isEmpty() || f.matches(query))
                .filter(f -> districtFilter == null || "সব জেলা".equals(districtFilter) || f.district.equalsIgnoreCase(districtFilter))
                .collect(Collectors.toList());

        displayCount = 6;
        renderGrid();
    }

    private void renderGrid() {
        if (warehouseContainer == null) {
            return;
        }

        warehouseContainer.getChildren().clear();

        int limit = Math.min(displayCount, filteredFacilities.size());
        for (int i = 0; i < limit; i++) {
            int column = i % 3;
            int row = i / 3;
            warehouseContainer.add(createCard(filteredFacilities.get(i)), column, row);
        }

        if (loadMoreBtn != null) {
            boolean hasMore = limit < filteredFacilities.size();
            loadMoreBtn.setVisible(hasMore);
            loadMoreBtn.setManaged(hasMore);
        }

        updateKpiCards();
    }

    private void updateKpiCards() {
        List<StorageFacility> source = filteredFacilities.isEmpty() ? allFacilities : filteredFacilities;
        if (source.isEmpty()) {
            return;
        }

        int totalAvailable = source.stream().mapToInt(f -> parseIntSafe(f.available)).sum();
        int totalCapacity = source.stream().mapToInt(f -> parseIntSafe(f.capacity)).sum();
        double occupancyRate = totalCapacity == 0 ? 0 : ((totalCapacity - totalAvailable) * 100.0 / totalCapacity);
        double avgPrice = source.stream().mapToInt(f -> parseIntSafe(f.price)).average().orElse(0);
        long verifiedCount = source.stream().filter(f -> f.owner != null && !f.owner.isBlank()).count();

        if (lblKpiAvailableValue != null) {
            lblKpiAvailableValue.setText(toBanglaDigits(String.format(Locale.US, "%,d টন", totalAvailable)));
        }
        if (lblKpiAvailableTrend != null) {
            double ratio = totalCapacity == 0 ? 0 : (totalAvailable * 100.0 / totalCapacity);
            lblKpiAvailableTrend.setText("মোটের " + toBanglaDigits(String.format(Locale.US, "%.1f%%", ratio)) + " খালি");
        }
        if (lblKpiOccupancyValue != null) {
            lblKpiOccupancyValue.setText(toBanglaDigits(String.format(Locale.US, "%.0f%%", occupancyRate)));
        }
        if (lblKpiOccupancyTrend != null) {
            lblKpiOccupancyTrend.setText(occupancyRate >= 75 ? "উচ্চ দখল" : occupancyRate >= 45 ? "স্থিতিশীল" : "কম দখল");
        }
        if (lblKpiPriceValue != null) {
            lblKpiPriceValue.setText("৳ " + toBanglaDigits(String.format(Locale.US, "%,.0f", avgPrice)));
        }
        if (lblKpiPriceTrend != null) {
            lblKpiPriceTrend.setText("ফিল্টারভিত্তিক গড় মূল্য");
        }
        if (lblKpiVerifiedValue != null) {
            lblKpiVerifiedValue.setText(toBanglaDigits(String.valueOf(verifiedCount)));
        }
        if (lblKpiVerifiedTrend != null) {
            lblKpiVerifiedTrend.setText(verifiedCount == source.size() ? "সম্পূর্ণ নথিভুক্ত" : "আংশিক নথিভুক্ত");
        }
    }

    private VBox createCard(StorageFacility f) {
        VBox card = new VBox(15);
        card.getStyleClass().add("warehouse-card");
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label(f.title);
        title.getStyleClass().add("card-title");
        title.setWrapText(true);
        title.setMaxWidth(180);

        String statusText = getStatusText(f.vacancyRate);
        Label badge = new Label(statusText + " • " + f.vacancyRate + "% ফাঁকা");
        badge.getStyleClass().add("badge-black");
        badge.getStyleClass().add(getStatusClass(f.vacancyRate));

        HBox header = new HBox(10, title, new Region(), badge);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        Label typeTag = new Label(f.type);
        typeTag.getStyleClass().add("tag-grey");

        String locationText = f.district + ", " + f.upazila;
        VBox details = new VBox(10,
                createRow("📍", locationText),
                createRow("👤", f.owner),
                createRow("📦", f.available + "/" + f.capacity + " টন"),
                createRow("💰", "৳ " + f.price + "/টন", "detail-price")
        );
        details.getStyleClass().add("details-box");

        Label capacityLabel = new Label("খালি সক্ষমতা");
        capacityLabel.getStyleClass().add("section-header");

        ProgressBar capacityBar = new ProgressBar(Math.max(0, Math.min(1, f.vacancyRate / 100.0)));
        capacityBar.getStyleClass().add("capacity-bar");
        capacityBar.setMaxWidth(Double.MAX_VALUE);

        HBox crops = new HBox(5);
        if (f.crops != null) {
            Arrays.stream(f.crops).forEach(c -> crops.getChildren().add(createTag(c, "tag-white")));
        }

        HBox facilities = new HBox(5);
        if (f.facilities != null) {
            Arrays.stream(f.facilities).forEach(item -> facilities.getChildren().add(createTag(item, "tag-facility")));
        }

        Button contactBtn = new Button("📞 যোগাযোগ করুন");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);

        if (f.vacancyRate == 0) {
            contactBtn.setText("পূর্ণ - অপেক্ষমাণ তালিকা");
            contactBtn.setDisable(true);
            contactBtn.getStyleClass().add("btn-contact-disabled");
        } else {
            contactBtn.setOnAction(e -> openWhatsApp(f));
        }

        Label cropsLabel = new Label("ফসল");
        cropsLabel.getStyleClass().add("section-header");
        Label facilitiesLabel = new Label("সুবিধা");
        facilitiesLabel.getStyleClass().add("section-header");

        card.getChildren().addAll(
                header,
                typeTag,
                details,
                capacityLabel,
                capacityBar,
                cropsLabel,
                crops,
                facilitiesLabel,
                facilities,
                contactBtn
        );
        return card;
    }

    private void openWhatsApp(StorageFacility f) {
        try {
            String phone = normalizePhoneForWhatsApp(f.phone);
            String message = "আসসালামু আলাইকুম, আমি " + f.title + " সম্পর্কে তথ্য জানতে চাই।";
            String url = "https://wa.me/" + phone + "?text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                showInfo("WhatsApp", "ডিভাইস ব্রাউজার সাপোর্ট করছে না।\nলিংক: " + url);
                return;
            }

            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            showInfo("WhatsApp", "WhatsApp খুলতে সমস্যা হয়েছে। পরে আবার চেষ্টা করুন।");
        }
    }

    private String normalizePhoneForWhatsApp(String raw) {
        String digits = raw == null ? "" : raw.replaceAll("[^0-9]", "");
        if (digits.startsWith("880")) {
            return digits;
        }
        if (digits.startsWith("0")) {
            return "88" + digits;
        }
        return digits;
    }

    private int parseIntSafe(String value) {
        if (value == null) {
            return 0;
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String toBanglaDigits(String input) {
        StringBuilder result = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= '0' && c <= '9') {
                result.append((char) ('০' + (c - '0')));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private HBox createRow(String icon, String text) {
        return createRow(icon, text, null);
    }

    private HBox createRow(String icon, String text, String customTextClass) {
        Label iconLbl = new Label(icon);
        iconLbl.getStyleClass().add("detail-icon");

        Label lbl = new Label(text);
        lbl.getStyleClass().add("detail-text");
        if (customTextClass != null) {
            lbl.getStyleClass().add(customTextClass);
        }

        HBox row = new HBox(10, iconLbl, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label createTag(String text, String styleClass) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(styleClass);
        return lbl;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private String getStatusText(int vacancyRate) {
        if (vacancyRate == 0) {
            return "পূর্ণ";
        }
        if (vacancyRate < 25) {
            return "সীমিত";
        }
        return "উপলব্ধ";
    }

    private String getStatusClass(int vacancyRate) {
        if (vacancyRate == 0) {
            return "badge-full";
        }
        if (vacancyRate < 25) {
            return "badge-limited";
        }
        return "badge-available";
    }

    private static class StorageFacility {
        String title;
        String type;
        String district;
        String upazila;
        String owner;
        String capacity;
        String available;
        String price;
        int vacancyRate;
        String phone;
        String[] crops;
        String[] facilities;

        StorageFacility(String title, String type, String district, String upazila, String owner,
                        String capacity, String available, String price, int vacancyRate, String phone,
                        String[] crops, String[] facilities) {
            this.title = title;
            this.type = type;
            this.district = district;
            this.upazila = upazila;
            this.owner = owner;
            this.capacity = capacity;
            this.available = available;
            this.price = price;
            this.vacancyRate = vacancyRate;
            this.phone = phone;
            this.crops = crops;
            this.facilities = facilities;
        }

        boolean matches(String query) {
            return title.toLowerCase().contains(query)
                    || district.toLowerCase().contains(query)
                    || upazila.toLowerCase().contains(query)
                    || owner.toLowerCase().contains(query)
                    || type.toLowerCase().contains(query);
        }
    }
}
