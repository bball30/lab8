package clientModule.localization;

import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class LanguageBundles {
    public static final ResourceBundle bundleRu = ResourceBundle.getBundle("clientModule.localization.bundleRu");
    public static final ResourceBundle bundleEt = ResourceBundle.getBundle("clientModule.localization.bundleEt");
    public static final ResourceBundle bundleFr = ResourceBundle.getBundle("clientModule.localization.bundleFr");
    public static final ResourceBundle bundleEs = ResourceBundle.getBundle("clientModule.localization.bundleEs");

    private static String currentLanguage = "Русский";

    public static ResourceBundle getBundle(String language) {
        switch (language) {
            case "Русский":
                return bundleRu;
            case "Eestlane":
                return bundleEt;
            case "Français":
                return bundleFr;
            case "Español":
                return bundleEs;
        }
        return null;
    }

    public static ResourceBundle getCurrentBundle() {
        return getBundle(currentLanguage);
    }

    public static void setCurrentLanguage(String language) {
        currentLanguage = language;
    }

}
