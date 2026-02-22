package com.example.demo1.app.ui;

import com.example.demo1.app.controller.MainLayoutController;
import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TopbarController {

    private enum CalendarLanguage {
        BANGLA("বাংলা", new Locale("bn", "BD")),
        ENGLISH("English", Locale.ENGLISH);

        private final String label;
        private final Locale locale;

        CalendarLanguage(String label, Locale locale) {
            this.label = label;
            this.locale = locale;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static final DayOfWeek[] WEEK_ORDER = {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
    };
    private static final String[] BANGLA_MONTH_NAMES = {
            "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
            "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    };

    private MainLayoutController mainLayoutController;

    @FXML
    private Label lblProfileName;

    @FXML
    private Label lblProfileMeta;

    @FXML
    public void initialize() {
        updateProfileLabel();
    }

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
        updateProfileLabel();
    }

    @FXML
    private void handleLogout() {
        if (mainLayoutController != null) {
            mainLayoutController.handleLogout();
        }
    }

    @FXML
    private void handleCalendarClick() {
        showCalendarDialog();
    }

    private void showCalendarDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("ক্যালেন্ডার");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        YearMonth[] activeMonth = {YearMonth.now()};
        CalendarLanguage[] activeLanguage = {CalendarLanguage.BANGLA};

        Label monthTitle = new Label();
        monthTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: #1e5128;");

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(6);
        calendarGrid.setVgap(6);
        calendarGrid.setPadding(new Insets(4, 0, 0, 0));

        for (int i = 0; i < 7; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 7.0);
            calendarGrid.getColumnConstraints().add(cc);
        }

        Button btnPrev = new Button("◀");
        Button btnNext = new Button("▶");
        btnPrev.setStyle("-fx-font-weight: 700;");
        btnNext.setStyle("-fx-font-weight: 700;");

        ComboBox<CalendarLanguage> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll(CalendarLanguage.BANGLA, CalendarLanguage.ENGLISH);
        languageCombo.setValue(CalendarLanguage.BANGLA);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(10, btnPrev, monthTitle, btnNext, spacer, new Label("মাস ভাষা:"), languageCombo);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label helperText = new Label("ডিফল্ট: বাংলা পঞ্জিকার মাস (যেমন বৈশাখ)। চাইলে English মাস বেছে নিতে পারবেন।");
        helperText.setStyle("-fx-text-fill: #4b5b73; -fx-font-size: 12px;");

        Runnable render = () -> renderCalendar(calendarGrid, monthTitle, activeMonth[0], activeLanguage[0]);
        render.run();

        btnPrev.setOnAction(e -> {
            activeMonth[0] = activeMonth[0].minusMonths(1);
            render.run();
        });
        btnNext.setOnAction(e -> {
            activeMonth[0] = activeMonth[0].plusMonths(1);
            render.run();
        });
        languageCombo.setOnAction(e -> {
            CalendarLanguage selected = languageCombo.getValue();
            activeLanguage[0] = selected == null ? CalendarLanguage.BANGLA : selected;
            render.run();
        });

        VBox content = new VBox(10, topRow, helperText, calendarGrid);
        content.setPadding(new Insets(12));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(560);

        dialog.showAndWait();
    }

    private void renderCalendar(GridPane grid, Label monthTitle, YearMonth month, CalendarLanguage language) {
        grid.getChildren().clear();

        Locale locale = language.locale;
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("d", locale);
        DateTimeFormatter weekFmt = DateTimeFormatter.ofPattern("EEE", locale);
        DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("MMMM yyyy", locale);

        if (language == CalendarLanguage.BANGLA) {
            monthTitle.setText(getBanglaMonthTitle(month.atDay(1)));
        } else {
            monthTitle.setText(month.atDay(1).format(monthFmt));
        }

        for (int col = 0; col < WEEK_ORDER.length; col++) {
            DayOfWeek dow = WEEK_ORDER[col];
            Label header = new Label(LocalDate.now().with(dow).format(weekFmt));
            header.setMaxWidth(Double.MAX_VALUE);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-font-weight: 700; -fx-text-fill: #1e5128;");
            GridPane.setHgrow(header, Priority.ALWAYS);
            grid.add(header, col, 0);
        }

        LocalDate first = month.atDay(1);
        int startCol = dayOfWeekToColumn(first.getDayOfWeek());
        int days = month.lengthOfMonth();
        LocalDate today = LocalDate.now();

        int col = startCol;
        int row = 1;
        for (int d = 1; d <= days; d++) {
            LocalDate date = month.atDay(d);
            String dayText = language == CalendarLanguage.BANGLA
                    ? toBanglaDigits(String.valueOf(getBanglaDayOfMonth(date)))
                    : date.format(dayFmt);
            Label dayLabel = new Label(dayText);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setMinHeight(34);
            dayLabel.setAlignment(Pos.CENTER);

            if (date.equals(today)) {
                dayLabel.setStyle("-fx-background-color: #4e944f; -fx-background-radius: 8; -fx-text-fill: white; -fx-font-weight: 800;");
            } else {
                dayLabel.setStyle("-fx-background-color: #f3f8f3; -fx-background-radius: 8; -fx-text-fill: #223629;");
            }

            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            grid.add(dayLabel, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private int dayOfWeekToColumn(DayOfWeek dow) {
        for (int i = 0; i < WEEK_ORDER.length; i++) {
            if (WEEK_ORDER[i] == dow) {
                return i;
            }
        }
        return 0;
    }

    private String getBanglaMonthTitle(LocalDate date) {
        String month = BANGLA_MONTH_NAMES[getBanglaMonthIndex(date)];
        int year = isOnOrAfter(date, 4, 14) ? date.getYear() - 593 : date.getYear() - 594;
        return month + " " + toBanglaDigits(String.valueOf(year));
    }

    private int getBanglaDayOfMonth(LocalDate date) {
        LocalDate monthStart = getBanglaMonthStart(date);
        long day = ChronoUnit.DAYS.between(monthStart, date) + 1;
        return (int) Math.max(1, day);
    }

    private LocalDate getBanglaMonthStart(LocalDate date) {
        int monthIndex = getBanglaMonthIndex(date);
        int year = date.getYear();

        return switch (monthIndex) {
            case 0 -> LocalDate.of(year, 4, 14);   // Boishakh
            case 1 -> LocalDate.of(year, 5, 15);   // Joishtho
            case 2 -> LocalDate.of(year, 6, 15);   // Asharh
            case 3 -> LocalDate.of(year, 7, 16);   // Srabon
            case 4 -> LocalDate.of(year, 8, 16);   // Bhadro
            case 5 -> LocalDate.of(year, 9, 16);   // Ashwin
            case 6 -> LocalDate.of(year, 10, 17);  // Kartik
            case 7 -> LocalDate.of(year, 11, 16);  // Agrahayan
            case 8 -> LocalDate.of(year, 12, 16);  // Poush
            case 9 -> LocalDate.of(year, 1, 15);   // Magh
            case 10 -> LocalDate.of(year, 2, 14);  // Falgun
            default -> LocalDate.of(year, 3, 15);  // Chaitra
        };
    }

    private int getBanglaMonthIndex(LocalDate date) {
        if (isInRange(date, 4, 14, 5, 14)) return 0;
        if (isInRange(date, 5, 15, 6, 14)) return 1;
        if (isInRange(date, 6, 15, 7, 15)) return 2;
        if (isInRange(date, 7, 16, 8, 15)) return 3;
        if (isInRange(date, 8, 16, 9, 15)) return 4;
        if (isInRange(date, 9, 16, 10, 16)) return 5;
        if (isInRange(date, 10, 17, 11, 15)) return 6;
        if (isInRange(date, 11, 16, 12, 15)) return 7;
        if (isInRange(date, 12, 16, 1, 14)) return 8;
        if (isInRange(date, 1, 15, 2, 13)) return 9;
        if (isInRange(date, 2, 14, 3, 14)) return 10;
        return 11;
    }

    private boolean isInRange(LocalDate date, int startMonth, int startDay, int endMonth, int endDay) {
        boolean afterStart = isOnOrAfter(date, startMonth, startDay);
        boolean beforeEnd = isOnOrBefore(date, endMonth, endDay);
        if (startMonth <= endMonth) {
            return afterStart && beforeEnd;
        }
        return afterStart || beforeEnd;
    }

    private boolean isOnOrAfter(LocalDate date, int month, int day) {
        if (date.getMonthValue() > month) return true;
        if (date.getMonthValue() < month) return false;
        return date.getDayOfMonth() >= day;
    }

    private boolean isOnOrBefore(LocalDate date, int month, int day) {
        if (date.getMonthValue() < month) return true;
        if (date.getMonthValue() > month) return false;
        return date.getDayOfMonth() <= day;
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

        if (fullName == null || fullName.isBlank()) {
            lblProfileName.setText("User");
            if (lblProfileMeta != null) {
                lblProfileMeta.setText("Unknown profile");
            }
            return;
        }

        lblProfileName.setText(fullName.trim());
        if (lblProfileMeta != null) {
            String username = user.getUsername();
            lblProfileMeta.setText((username == null || username.isBlank()) ? "No username" : "@" + username.trim());
        }
    }
}
