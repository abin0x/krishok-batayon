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
        routes.put("btnHome", new NavItem("/com/example/demo1/app/fxml/dashboard/dashboard.fxml", "🏠 হোম"));
        routes.put("btnAdvisory", new NavItem("/com/example/demo1/app/fxml/features/planning-content.fxml", "📅 পরিকল্পনা ও হিসাব"));
        routes.put("btnStorage", new NavItem("/com/example/demo1/app/fxml/features/storage-content.fxml", "🏪 গুদাম ও সংরক্ষণ"));
        routes.put("btnLocalManagement", new NavItem("/com/example/demo1/app/fxml/features/labor-content.fxml", "👷 শ্রমিক ব্যবস্থাপনা"));
        routes.put("btnMachinery", new NavItem("/com/example/demo1/app/fxml/features/machinery-content.fxml", "🚜 যন্ত্রপাতি"));
        routes.put("btnFarmManagement", new NavItem("/com/example/demo1/app/fxml/features/farm-management-content.fxml", "🌱 খামার ব্যবস্থাপনা"));
        routes.put("btnSmartDiagnostic", new NavItem("/com/example/demo1/app/fxml/features/smart-diagnostic-content.fxml", "🩺 স্মার্ট ডায়াগনস্টিক"));
        routes.put("btnDigitalHat", new NavItem("/com/example/demo1/app/fxml/features/digital-hat-content.fxml", "🛒 ডিজিটাল হাট"));
        routes.put("btnCropInsurance", new NavItem("/com/example/demo1/app/fxml/features/crop-insurance-content.fxml", "🛡️ শস্য বীমা"));
        routes.put("btnMarketFinance", new NavItem("/com/example/demo1/app/fxml/features/market-finance-content.fxml", "📈 বাজার দর ও বিশ্লেষণ"));
        routes.put("btnGovtSchemes", new NavItem("/com/example/demo1/app/fxml/features/govt-schemes.fxml", "🏛️ সরকারি সুবিধা ও ভর্তুকি"));
        routes.put("btnExpertChat", new NavItem("/com/example/demo1/app/fxml/features/expert-chat-content.fxml", "💬 বিশেষজ্ঞ চ্যাট"));
        routes.put("btnLearningHub", new NavItem("/com/example/demo1/app/fxml/features/learning-hub-content.fxml", "📚 কৃষি পাঠশালা"));
        routes.put("btnEmergencyHelp", new NavItem("/com/example/demo1/app/fxml/features/emergency-help.fxml", "📞 সরকারি সেবা ও সাহায্য"));
        routes.put("btnProfile", new NavItem("/com/example/demo1/app/fxml/features/profile-view.fxml", "👤 প্রোফাইল"));
        SIDEBAR_ROUTES = Collections.unmodifiableMap(routes);
    }

    public static Map<String, NavItem> sidebarRoutes() {
        return SIDEBAR_ROUTES;
    }
}
