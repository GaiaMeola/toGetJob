package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.printer.Printer;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileSystemStudentDao implements StudentDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/Student.txt";

    @Override
    public void saveStudent(Student student) {
        if (studentExists(student.getUsername())) {
            Printer.print("Lo studente con username " + student.getUsername() + " esiste già.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(student.getName() + ";" + student.getSurname() + ";" + student.getUsername() + ";" +
                    student.getEmailAddress() + ";" + student.getPassword() + ";" + student.getRole() + ";" +
                    student.getDateOfBirth() + ";" +
                    student.getPhoneNumber() + ";" +
                    student.getDegrees() + ";" +
                    student.getCourseAttended() + ";" +
                    student.getCertifications() + ";" +
                    student.getWorkExperiences() + ";" +
                    student.getSkills() + ";" +
                    student.getAvailability());
            writer.newLine();
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Student> getStudent(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 14 && data[2].trim().equals(username)) {  // Assicurati che ci siano almeno 14 elementi (per tutti i dati)
                    // Trova lo studente corrispondente allo username
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.STUDENT) {
                        // Costruisci lo Student con i vecchi campi e i nuovi
                        LocalDate dateOfBirth = LocalDate.parse(data[6].trim());  // Assicurati che sia nel formato corretto
                        String phoneNumber = data[7].trim();
                        List<String> degrees = Arrays.asList(data[8].split(","));
                        List<String> courseAttended = Arrays.asList(data[9].split(","));
                        List<String> certifications = Arrays.asList(data[10].split(","));
                        List<String> workExperiences = Arrays.asList(data[11].split(","));
                        List<String> skills = Arrays.asList(data[12].split(","));
                        String availability = data[13].trim();

                        // Crea lo Student completo con tutti i campi
                        Student student = new Student(
                                data[0].trim(),  // name
                                data[1].trim(),  // surname
                                data[2].trim(),  // username
                                data[3].trim(),  // emailAddress
                                data[4].trim(),  // password
                                role,  // role
                                dateOfBirth,  // dateOfBirth
                                phoneNumber,  // phoneNumber
                                degrees,  // degrees
                                courseAttended,  // courseAttended
                                certifications,  // certifications
                                workExperiences,  // workExperiences
                                skills,  // skills
                                availability,  // availability
                                new ArrayList<>()  // jobApplications, vuoto per ora
                        );
                        return Optional.of(student);
                    }
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        } catch (DateTimeParseException e) {
            Printer.print("Errore nel parsing della data di nascita: " + e.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 14) {  // Assicurati che ci siano almeno 14 elementi (per tutti i dati)
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.STUDENT) {
                        // Parso i nuovi campi
                        LocalDate dateOfBirth = LocalDate.parse(data[6].trim());  // Assicurati che la data sia nel formato corretto
                        String phoneNumber = data[7].trim();
                        List<String> degrees = Arrays.asList(data[8].split(","));
                        List<String> courseAttended = Arrays.asList(data[9].split(","));
                        List<String> certifications = Arrays.asList(data[10].split(","));
                        List<String> workExperiences = Arrays.asList(data[11].split(","));
                        List<String> skills = Arrays.asList(data[12].split(","));
                        String availability = data[13].trim();

                        // Crea lo Student completo con tutti i campi
                        Student student = new Student(
                                data[0].trim(),  // name
                                data[1].trim(),  // surname
                                data[2].trim(),  // username
                                data[3].trim(),  // emailAddress
                                data[4].trim(),  // password
                                role,  // role
                                dateOfBirth,  // dateOfBirth
                                phoneNumber,  // phoneNumber
                                degrees,  // degrees
                                courseAttended,  // courseAttended
                                certifications,  // certifications
                                workExperiences,  // workExperiences
                                skills,  // skills
                                availability,  // availability
                                new ArrayList<>()  // jobApplications, vuoto per ora
                        );
                        students.add(student);
                    }
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Printer.print("Errore nel parsing del ruolo: " + e.getMessage());
        } catch (DateTimeParseException e) {
            Printer.print("Errore nel parsing della data di nascita: " + e.getMessage());
        }
        return students;
    }

    @Override
    public boolean updateStudent(Student student) {
        if (!studentExists(student.getUsername())) {
            return false; // Lo studente non esiste, quindi non possiamo aggiornarlo.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(student.getUsername())) {
                    // Trova la riga dello studente da aggiornare e sostituiscila con i nuovi dati
                    line = student.getName() + ";" + student.getSurname() + ";" + student.getUsername() + ";" +
                            student.getEmailAddress() + ";" + student.getPassword() + ";" + student.getRole() + ";" +
                            student.getDateOfBirth() + ";" + student.getPhoneNumber() + ";" +
                            String.join(",", student.getDegrees()) + ";" +
                            String.join(",", student.getCourseAttended()) + ";" +
                            String.join(",", student.getCertifications()) + ";" +
                            String.join(",", student.getWorkExperiences()) + ";" +
                            String.join(",", student.getSkills()) + ";" +
                            student.getAvailability();
                }
                lines.add(line); // Aggiungi la riga al buffer
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
            return false;
        }

        // Scrivi le linee aggiornate nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine(); // Aggiungi una nuova riga
            }
        } catch (IOException e) {
            Printer.print("Errore durante la scrittura del file: " + e.getMessage());
            return false;
        }

        return true; // L'aggiornamento è andato a buon fine
    }

    @Override
    public boolean deleteStudent(String username) {
        if (!studentExists(username)) {
            return false; // Lo studente non esiste, quindi non possiamo eliminarlo.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && !data[2].trim().equals(username)) {
                    // Aggiungi solo le righe che non corrispondono allo studente da eliminare
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
            return false;
        }

        // Riscrivi il file senza lo studente eliminato
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
    public boolean studentExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 5 && data[2].trim().equals(username)) {  // username è al terzo posto
                    return true;  // Studente trovato
                }
            }
        } catch (IOException e) {
            Printer.print("Errore durante la lettura del file: " + e.getMessage());
        }
        return false;  // Studente non trovato
    }
}
