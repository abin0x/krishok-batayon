package com.example.demo1.app.controller;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class TopbarController {

    private static final String[] WEEK_HEADERS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] BANGLA_MONTH_NAMES = {
            "Boishakh", "Joishtho", "Asharh", "Srabon", "Bhadro", "Ashwin",
            "Kartik", "Agrahayan", "Poush", "Magh", "Falgun", "Chaitra"
    };

    //4 no mas mane holo april er 14 tarikhe boishakh mas shuru hoi
    private static final int[] B_START_MONTH = {4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3};//ata holo english maser name:april
    private static final int[] B_START_DAY = {14, 15, 15, 16, 16, 16, 17, 16, 16, 15, 14, 15};//ata holo english maser date,14-april

    private MainLayoutController mainLayoutController;

    @FXML private Label lblProfileName;//ata holo topbar er profile name show korar label
    @FXML private Label lblProfileMeta;//ata holo topbar a username show korbe

    @FXML
    public void initialize() {
        updateProfileLabel();//ata call korbe jate kore topbar er profile name and username show kore
    }

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
        updateProfileLabel();
    }

    @FXML
    private void handleLogout() {//ata call hobe jodi user logout button click kore
        if (mainLayoutController != null) {
            mainLayoutController.handleLogout();
        }
    }

    @FXML//ata call hobe jodi user calendar button click kore
    private void handleCalendarClick() {
        Dialog<Void> dialog = new Dialog<>();//ata holo calendar dialog create korar code
        dialog.setTitle("Calendar");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        YearMonth[] activeMonth = {YearMonth.now()};//ata holo active month track korar jonno, initially current month set kora hoyeche

        Label monthTitle = new Label();
        monthTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: #1e5128;");

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(6);
        calendarGrid.setVgap(6);
        calendarGrid.setPadding(new Insets(4, 0, 0, 0));

        for (int i = 0; i < 7; i++) {//ata holo calendar grid er column constraints set korar code, jate kore 7 column equal width pabe
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 7.0);
            calendarGrid.getColumnConstraints().add(cc);
        }

        Button btnPrev = new Button("<");
        Button btnNext = new Button(">");
        btnPrev.setStyle("-fx-font-weight: 700;");
        btnNext.setStyle("-fx-font-weight: 700;");

        Runnable render = () -> renderCalendar(calendarGrid, monthTitle, activeMonth[0]);
        render.run();

        btnPrev.setOnAction(e -> {
            activeMonth[0] = activeMonth[0].minusMonths(1);
            render.run();
        });
        btnNext.setOnAction(e -> {
            activeMonth[0] = activeMonth[0].plusMonths(1);
            render.run();
        });

        HBox topRow = new HBox(10, btnPrev, monthTitle, btnNext);
        topRow.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(10, topRow, calendarGrid);
        content.setPadding(new Insets(12));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(560);

        dialog.showAndWait();
    }

    private void renderCalendar(GridPane grid, Label monthTitle, YearMonth month) {
        grid.getChildren().clear();
        monthTitle.setText(getBanglaMonthTitle(month.atDay(1)));

        for (int col = 0; col < WEEK_HEADERS.length; col++) {
            Label header = new Label(WEEK_HEADERS[col]);
            header.setMaxWidth(Double.MAX_VALUE);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-font-weight: 700; -fx-text-fill: #1e5128;");
            GridPane.setHgrow(header, Priority.ALWAYS);
            grid.add(header, col, 0);
        }

        LocalDate first = month.atDay(1);
        int col = first.getDayOfWeek().getValue() % 7;
        int row = 1;
        LocalDate today = LocalDate.now();

        for (int d = 1; d <= month.lengthOfMonth(); d++) {
            LocalDate date = month.atDay(d);
            Label dayLabel = new Label(toBanglaDigits(String.valueOf(getBanglaDayOfMonth(date))));
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setMinHeight(34);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setStyle(date.equals(today)
                    ? "-fx-background-color: #4e944f; -fx-background-radius: 8; -fx-text-fill: white; -fx-font-weight: 800;"
                    : "-fx-background-color: #f3f8f3; -fx-background-radius: 8; -fx-text-fill: #223629;");

            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            grid.add(dayLabel, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private String getBanglaMonthTitle(LocalDate date) {
        int idx = getBanglaMonthIndex(date);
        int year = (date.getMonthValue() > 4 || (date.getMonthValue() == 4 && date.getDayOfMonth() >= 14))
                ? date.getYear() - 593 //jodi 14 ba er beshi hoy april maser, tahole current year theke 593 minus korbe, nahole 594 minus korbe
                : date.getYear() - 594;
        return BANGLA_MONTH_NAMES[idx] + " " + toBanglaDigits(String.valueOf(year));//ata holo bangla month title generate korar code, jekhane month name er sathe year o show korbe, year ta bangla digit e convert kora hobe
    }


    //bangla mas er koto traikh set aber kora
    private int getBanglaDayOfMonth(LocalDate date) {
        return (int) (ChronoUnit.DAYS.between(getBanglaMonthStart(date), date) + 1);//ata holo bangla mas er date calculate korar code, jekhane getBanglaMonthStart method call kore current date theke bangla mas er start date er modhye koto din por ase ta calculate kore, tarpor 1 add kore day of month return kore
    }

    //bangla mas er start date calculate korar code, jekhane getBanglaMonthIndex method call kore current date er bangla mas er index ber kore, tarpor B_START_MONTH and B_START_DAY array theke oi index er month and day niye LocalDate return kore
    private LocalDate getBanglaMonthStart(LocalDate date) {
        int idx = getBanglaMonthIndex(date);
        int month = B_START_MONTH[idx];
        int day = B_START_DAY[idx];
        int year = date.getYear();
        if (date.getMonthValue() < month || (date.getMonthValue() == month && date.getDayOfMonth() < day)) {
            year--;
        }
        return LocalDate.of(year, month, day);
    }

    private int getBanglaMonthIndex(LocalDate date) {
        int value = date.getMonthValue() * 100 + date.getDayOfMonth();
        for (int i = 0; i < 12; i++) {
            int start = B_START_MONTH[i] * 100 + B_START_DAY[i];
            int end = B_START_MONTH[(i + 1) % 12] * 100 + B_START_DAY[(i + 1) % 12];
            boolean inRange = (start < end) ? (value >= start && value < end) : (value >= start || value < end);
            if (inRange) {
                return i;
            }
        }
        return 11;
    }

    private String toBanglaDigits(String input) {
        return input
                .replace('0', '০')
                .replace('1', '১')
                .replace('2', '২')
                .replace('3', '৩')
                .replace('4', '৪')
                .replace('5', '৫')
                .replace('6', '৬')
                .replace('7', '৭')
                .replace('8', '৮')
                .replace('9', '৯');
    }

    private void updateProfileLabel() {
        if (lblProfileName == null) {
            return;
        }

        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            lblProfileName.setText("User");
            if (lblProfileMeta != null) {
                lblProfileMeta.setText("Not signed in");
            }
            return;
        }

        String fullName = user.getName();
        if (fullName == null || fullName.isBlank()) {
            fullName = user.getUsername();
        }
        lblProfileName.setText((fullName == null || fullName.isBlank()) ? "User" : fullName.trim());

        if (lblProfileMeta != null) {
            String username = user.getUsername();
            lblProfileMeta.setText((username == null || username.isBlank()) ? "No username" : "@" + username.trim());
        }
    }
}
