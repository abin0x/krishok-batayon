package com.example.demo1.app.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NavigationManager {
    private NavigationManager() {}

    public record NavItem(String viewPath, String displayName) {}

    private static final Map<String, NavItem> SIDEBAR_ROUTES;

    static {
        Map<String, NavItem> routes = new LinkedHashMap<>();
        routes.put("btnHome", new NavItem("/fxml/dashboard/dashboard.fxml", "হোম"));
        routes.put("btnAdvisory", new NavItem("/fxml/features/planning-content.fxml", "পরিকল্পনা ও হিসাব"));
        routes.put("btnStorage", new NavItem("/fxml/features/WarehouseView.fxml", "গুদাম ও সংরক্ষণ"));
        routes.put("btnLocalManagement", new NavItem("/fxml/features/LocalManagement.fxml", "শ্রমিক ব্যবস্থাপনা"));
        routes.put("btnMachinery", new NavItem("/fxml/features/MachineryView.fxml", "যন্ত্রপাতি"));
        routes.put("btnSmartDiagnostic", new NavItem("/fxml/features/smart-diagnostic-content.fxml", "স্মার্ট ডায়াগনস্টিক"));
        routes.put("btnDigitalHat", new NavItem("/fxml/features/digital-hat-content.fxml", "ডিজিটাল হাট"));
        routes.put("btnMarketFinance", new NavItem("/fxml/features/market-finance-content.fxml", "বাজার দর ও বিশ্লেষণ"));
        routes.put("btnGovtSchemes", new NavItem("/fxml/features/govt-schemes.fxml", "সরকারি সুবিধা ও ভর্তুকি"));
        routes.put("btnEmergencyHelp", new NavItem("/fxml/features/emergency-help.fxml", "সরকারি সেবা ও সাহায্য"));
        routes.put("btnProfile", new NavItem("/fxml/features/profile-view.fxml", "প্রোফাইল"));
        SIDEBAR_ROUTES = Collections.unmodifiableMap(routes);
    }

    public static Map<String, NavItem> sidebarRoutes() {
        return SIDEBAR_ROUTES;
    }
}
