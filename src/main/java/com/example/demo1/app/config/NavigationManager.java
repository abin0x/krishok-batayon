package com.example.demo1.app.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NavigationManager {
    private NavigationManager() {}

    public record NavItem(String viewPath, String label) {}

    private static final Map<String, NavItem> SIDEBAR_ROUTES;

    static {
        Map<String, NavItem> routes = new LinkedHashMap<>();
        routes.put("btnHome", new NavItem("/com/example/demo1/app/fxml/dashboard/dashboard.fxml", "\uD83C\uDFE0 \u09B9\u09CB\u09AE"));
        routes.put("btnAdvisory", new NavItem("/com/example/demo1/app/fxml/features/planning-content.fxml", "\uD83D\uDCC5 \u09AA\u09B0\u09BF\u0995\u09B2\u09CD\u09AA\u09A8\u09BE \u0993 \u09B9\u09BF\u09B8\u09BE\u09AC"));
        routes.put("btnStorage", new NavItem("/com/example/demo1/app/fxml/features/WarehouseView.fxml", "\uD83C\uDFEA \u0997\u09C1\u09A6\u09BE\u09AE \u0993 \u09B8\u0982\u09B0\u0995\u09CD\u09B7\u09A3"));
        routes.put("btnLocalManagement", new NavItem("/com/example/demo1/app/fxml/features/labor-content.fxml", "\uD83D\uDC77 \u09B6\u09CD\u09B0\u09AE\u09BF\u0995 \u09AC\u09CD\u09AF\u09AC\u09B8\u09CD\u09A5\u09BE\u09AA\u09A8\u09BE"));
        routes.put("btnMachinery", new NavItem("/com/example/demo1/app/fxml/features/machinery-content.fxml", "\uD83D\uDE9C \u09AF\u09A8\u09CD\u09A4\u09CD\u09B0\u09AA\u09BE\u09A4\u09BF"));
        routes.put("btnFarmManagement", new NavItem("/com/example/demo1/app/fxml/features/farm-management-content.fxml", "\uD83C\uDF31 \u0996\u09BE\u09AE\u09BE\u09B0 \u09AC\u09CD\u09AF\u09AC\u09B8\u09CD\u09A5\u09BE\u09AA\u09A8\u09BE"));
        routes.put("btnSmartDiagnostic", new NavItem("/com/example/demo1/app/fxml/features/smart-diagnostic-content.fxml", "\uD83E\uDDEA \u09B8\u09CD\u09AE\u09BE\u09B0\u09CD\u099F \u09A1\u09BE\u09DF\u09BE\u0997\u09A8\u09B8\u09CD\u099F\u09BF\u0995"));
        routes.put("btnDigitalHat", new NavItem("/com/example/demo1/app/fxml/features/digital-hat-content.fxml", "\uD83D\uDED2 \u09A1\u09BF\u099C\u09BF\u099F\u09BE\u09B2 \u09B9\u09BE\u099F"));
        routes.put("btnMarketFinance", new NavItem("/com/example/demo1/app/fxml/features/market-finance-content.fxml", "\uD83D\uDCC8 \u09AC\u09BE\u099C\u09BE\u09B0 \u09A6\u09B0 \u0993 \u09AC\u09BF\u09B6\u09CD\u09B2\u09C7\u09B7\u09A3"));
        routes.put("btnGovtSchemes", new NavItem("/com/example/demo1/app/fxml/features/govt-schemes.fxml", "\uD83C\uDFDB\uFE0F \u09B8\u09B0\u0995\u09BE\u09B0\u09BF \u09B8\u09C1\u09AC\u09BF\u09A7\u09BE \u0993 \u09AD\u09B0\u09CD\u09A4\u09C1\u0995\u09BF"));
        routes.put("btnEmergencyHelp", new NavItem("/com/example/demo1/app/fxml/features/emergency-help.fxml", "\uD83D\uDCDE \u09B8\u09B0\u0995\u09BE\u09B0\u09BF \u09B8\u09C7\u09AC\u09BE \u0993 \u09B8\u09BE\u09B9\u09BE\u09AF\u09CD\u09AF"));
        routes.put("btnProfile", new NavItem("/com/example/demo1/app/fxml/features/profile-view.fxml", "\uD83D\uDC64 \u09AA\u09CD\u09B0\u09CB\u09AB\u09BE\u0987\u09B2"));
        SIDEBAR_ROUTES = Collections.unmodifiableMap(routes);
    }

    public static Map<String, NavItem> sidebarRoutes() {
        return SIDEBAR_ROUTES;
    }
}
