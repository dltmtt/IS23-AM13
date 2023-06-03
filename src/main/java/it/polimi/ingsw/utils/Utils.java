package it.polimi.ingsw.utils;

public class Utils {

    public static boolean showDebugInfo = true;

    public static void showDebugInfo(Exception e) {
        if (!showDebugInfo) {
            return;
        }

        String debugMessage = """
                DEBUG INFO:
                Exception message: %s
                Exception class: %s
                """.formatted(e.getMessage(), e.getClass());
        System.err.println(debugMessage);
    }

    public void setShowDebugInfo(boolean showDebugInfo) {
        Utils.showDebugInfo = showDebugInfo;
    }
}
