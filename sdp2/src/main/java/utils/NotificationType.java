package utils;

import java.util.Arrays;

public enum NotificationType {
    MAINTENANCE, REMINDER;

    public static NotificationType getTypeByString(String s) {
        return Arrays.asList(NotificationType.values()).stream().filter(obj -> obj.name().equals(s)).findFirst().orElse(null);
    }
}
