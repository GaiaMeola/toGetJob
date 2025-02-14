package org.example.togetjob;

import javafx.application.Application;
import org.example.togetjob.config.AppConfig;
import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.CliContext;
import org.example.togetjob.view.cli.concretestate.MainMenuState;
import org.example.togetjob.view.GUIContext;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static Context context;

    public static void main(String[] args) {
        try {
            AppConfig.loadConfigs();
        } catch (ConfigException e) {
            Printer.print("Error during configuration: " + e.getMessage());
            return;
        }

        setupDatabase();
        launchUI();
    }

    private static void setupDatabase() {
        String daoType = AppConfig.getDaoType();

        if ("jdbc".equalsIgnoreCase(daoType)) {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
            databaseConfig.setConfigLoader(AppConfig.getDaoConfig());

            try (Connection connection = databaseConfig.getConnection()) {
                if (connection != null) {
                    Printer.print("Database connection successful.");
                } else {
                    Printer.print("Error during connection.");
                }
            } catch (SQLException e) {
                Printer.print("Error during connection: " + e.getMessage());
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            Printer.print("DAO In-Memory.");
        } else if ("file system".equalsIgnoreCase(daoType)) {
            Printer.print("DAO FileSystem.");
        } else {
            Printer.print("DAO not found.");
            System.exit(0);
        }

    }

    private static void launchUI() {
        String uiType = AppConfig.getUiType();

        if ("cli".equalsIgnoreCase(uiType)) {
            launchCli();
        } else if ("gui".equalsIgnoreCase(uiType)) {
            launchGui();
        } else {
            Printer.print("UI not found.");
        }
    }

    private static void launchCli() {
        context = new CliContext();
        Printer.print("CLI...");
        context.setState(new MainMenuState());
        context.initialize();
    }

    private static void launchGui() {
        context = new GUIContext();
        Printer.print("GUI...");
        Application.launch(GUIContext.class);
    }
}
