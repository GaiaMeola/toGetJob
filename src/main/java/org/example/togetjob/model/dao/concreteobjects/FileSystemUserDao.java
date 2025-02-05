package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.printer.Printer;

import java.io.*;
import java.util.*;

public class FileSystemUserDao implements UserDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/User.txt";

    @Override
    public boolean saveUser(User user) {
        if (userExists(user.getUsername())) {
            Printer.print("L'utente con username " + user.getUsername() + " esiste già.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(user.getName() + ";" + user.getSurname() + ";" + user.getUsername() + ";" +
                    user.getEmailAddress() + ";" + user.getPassword() + ";" + user.getRole());
            writer.newLine();
            return true;
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(username)) {
                    // Trova l'utente corrispondente allo username
                    Role role = Role.valueOf(data[5].trim());
                    User user = (role == Role.STUDENT)
                            ? new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role)
                            : new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role);
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        }
        // Restituisce Optional.empty() se l'utente non è stato trovato
        return Optional.empty();
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Dividi la riga in base alla virgola
                String[] data = line.split(";");
                // Verifica che la riga contenga i dati corretti
                if (data.length >= 6) {
                    Role role = Role.valueOf(data[5].trim()); // Rimuovi gli spazi extra
                    User user = (role == Role.STUDENT)
                            ? new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role)
                            : new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(user.getUsername())) {
                    // Trova la riga dell'utente da aggiornare e sostituiscila con i nuovi dati
                    line = user.getName() + "," + user.getSurname() + "," + user.getUsername() + "," +
                            user.getEmailAddress() + "," + user.getPassword() + "," + user.getRole();
                }
                lines.add(line); // Aggiungi la riga al buffer
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
            return false;
        }

        // Riscrivi il file con i dati aggiornati
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Scrivi a capo
            }
            return true;
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteUser(String username) {
        if (!userExists(username)) {
            return false; // L'utente non esiste, quindi non possiamo eliminarlo.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && !data[2].trim().equals(username)) {
                    // Aggiungi solo le righe che non corrispondono all'utente da eliminare
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
            return false;
        }

        // Riscrivi il file senza l'utente eliminato
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Scrivi a capo
            }
            return true;
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(username)) {  // username è al terzo posto
                    return true;  // Utente trovato
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        }
        return false;  // Utente non trovato
    }

}