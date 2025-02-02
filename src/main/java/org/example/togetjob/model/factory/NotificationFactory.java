package org.example.togetjob.model.factory;

import org.example.togetjob.config.ConfigUILoader;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.pattern.CLINotification;
import org.example.togetjob.pattern.GUINotification;
import org.example.togetjob.pattern.Notification;

public class NotificationFactory {

    private static final String CONFIG_FILE = "ui.config.properties";

    private NotificationFactory() {
        //To not create instance
    }

    public static Notification createNotification() throws ConfigException {
        ConfigUILoader uiLoader;

        try {
            uiLoader = new ConfigUILoader(CONFIG_FILE);
        } catch (ConfigException e) {
            throw new ConfigException("Error during the configuration", e);
        }

        String uiType = uiLoader.getProperty("ui.type");

        return switch (uiType.toLowerCase()) {
            case "cli" -> new CLINotification();
            case "gui" -> new GUINotification();
            default -> throw new ConfigException("UI not found: " + uiType);
        };
    }
}
