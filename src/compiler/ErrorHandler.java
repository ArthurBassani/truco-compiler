package compiler;

import gui.TrucoGUI;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private static TrucoGUI currentGUI = null;
    private static List<String> errors = new ArrayList<>();
    private static boolean hasErrors = false;
    
    public static void setGUI(TrucoGUI gui) {
        currentGUI = gui;
        errors.clear();
        hasErrors = false;
    }
    
    public static void addError(String error) {
        errors.add(error);
        hasErrors = true;
        if (currentGUI != null) {
            currentGUI.appendError(error);
        }
    }
    
    public static void logRecovery(String message) {
        if (currentGUI != null) {
            currentGUI.appendError(message);
        }
    }
    
    public static boolean hasErrors() {
        return hasErrors;
    }
    
    public static List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public static void clear() {
        errors.clear();
        hasErrors = false;
        currentGUI = null;
    }
}