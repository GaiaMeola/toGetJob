package testing;

import org.example.togetjob.bean.LoginUserBean;
import org.example.togetjob.config.AppConfig;
import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.concreteobjects.DataBaseUserDao;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;

import java.sql.Connection;
import java.sql.SQLException;

public class Testing {

    private static final String STRING = "test successful";

    public static void main(String[] args) {
        try {
            AppConfig.loadConfigs();
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
        } catch (ConfigException e) {
            Printer.print("error");
        }

        testNullConfigDaoLoader();

        Testing testing = new Testing();
        testing.runTests();
    }

    public void runTests() {
        testEmail();
        testLoginUsername();
        testNullConfigDaoLoader();
    }

    public void testEmail() {
        try {
            saveUser();
        } catch (EmailAlreadyExistsException e) {
            Printer.print(STRING);
        }
    }

    public void testLoginUsername() {
        LoginBoundary loginBoundary = new LoginBoundary();
        String username = "WrongUsername";
        String password = "Password";

        LoginUserBean loginUserBean = new LoginUserBean();
        loginUserBean.setUsername(username);
        loginUserBean.setPassword(password);

        try {
            loginBoundary.login(loginUserBean);
        } catch (UserNotFoundException e) {
            Printer.print(STRING);
        }
    }

    public static void testNullConfigDaoLoader() {
        try {
            AbstractFactoryDaoSingleton.getFactoryDao();
        } catch (IllegalStateException e) {
            Printer.print(STRING);
        }
    }

    private void saveUser() throws UsernameTakeException, EmailAlreadyExistsException, DatabaseException {
        Student user = new Student("name", "surname", "username", "alreadyExisting@gmail.com", "password", Role.RECRUITER);
        DataBaseUserDao dataBaseUserDao = new DataBaseUserDao();
        dataBaseUserDao.saveUser(user);
    }
}
