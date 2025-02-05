package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.printer.Printer;

import java.io.*;
import java.util.*;

public class FileSystemRecruiterDao implements RecruiterDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/Recruiter.txt";

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        if (recruiterExists(recruiter.getUsername())) {
            Printer.print("Il recruiter con username " + recruiter.getUsername() + " esiste già.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(recruiter.getName() + ";" + recruiter.getSurname() + ";" + recruiter.getUsername() + ";" +
                    recruiter.getEmailAddress() + ";" + recruiter.getPassword() + ";" + recruiter.getRole() + ";" + recruiter.getCompanies());
            writer.newLine();
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && data[2].trim().equals(username)) {
                    // Trova il recruiter corrispondente allo username
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.RECRUITER) {
                        List<String> companies = Arrays.asList(data[6].split(","));
                        Recruiter recruiter = new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role, companies);
                        return Optional.of(recruiter);
                    }
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Recruiter> getAllRecruiter() {
        List<Recruiter> recruiters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7) { // Assicurati che ci siano almeno 7 colonne
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.RECRUITER) {
                        // Recupera la lista delle aziende (composte da più stringhe separate da punto e virgola)
                        List<String> companies = Arrays.asList(data[6].split(","));

                        Recruiter recruiter = new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role, companies);
                        recruiters.add(recruiter);
                    }
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        }
        return recruiters;
    }


    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && data[2].trim().equals(recruiter.getUsername())) {
                    // Trova la riga del recruiter da aggiornare e sostituiscila con i nuovi dati
                    String updatedLine = recruiter.getName() + ";" + recruiter.getSurname() + ";" + recruiter.getUsername() + ";" +
                            recruiter.getEmailAddress() + ";" + recruiter.getPassword() + ";" + recruiter.getRole() + ";" +
                            String.join(",", recruiter.getCompanies()); // Concatena le aziende separandole con un ";"
                    lines.add(updatedLine); // Aggiungi la riga aggiornata
                } else {
                    lines.add(line); // Aggiungi la riga non modificata
                }
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
    public boolean deleteRecruiter(String username) {
        if (!recruiterExists(username)) {
            return false; // Il recruiter non esiste, quindi non possiamo eliminarlo.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && !data[2].trim().equals(username)) {
                    // Aggiungi solo le righe che non corrispondono al recruiter da eliminare
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
            return false;
        }

        // Riscrivi il file senza il recruiter eliminato
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
    public boolean recruiterExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && data[2].trim().equals(username)) {  // username è al terzo posto
                    return true;  // Recruiter trovato
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        }
        return false;  // Recruiter non trovato
    }
}

