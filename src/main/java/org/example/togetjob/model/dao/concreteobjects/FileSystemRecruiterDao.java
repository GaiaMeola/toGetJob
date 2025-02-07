package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;

import java.io.*;
import java.util.*;

public class FileSystemRecruiterDao implements RecruiterDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/Recruiter.txt";

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        if (recruiterExists(recruiter.getUsername())) {
            return; // The recruiter with the given username already exists.
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(recruiter.getName() + ";" + recruiter.getSurname() + ";" + recruiter.getUsername() + ";" +
                    recruiter.getEmailAddress() + ";" + recruiter.getPassword() + ";" + recruiter.getRole() + ";" + recruiter.getCompanies());
            writer.newLine();
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && data[2].trim().equals(username)) {
                    // Find the recruiter corresponding to the username
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.RECRUITER) {
                        List<String> companies = Arrays.asList(data[6].split(","));
                        Recruiter recruiter = new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role, companies);
                        return Optional.of(recruiter);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
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
                if (data.length >= 7) { // Ensure there are at least 7 columns
                    Role role = Role.valueOf(data[5].trim());
                    if (role == Role.RECRUITER) {
                        // Retrieve the list of companies (separated by commas)
                        List<String> companies = Arrays.asList(data[6].split(","));

                        Recruiter recruiter = new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role, companies);
                        recruiters.add(recruiter);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
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
                    // Find the recruiter row to update and replace it with the new data
                    String updatedLine = recruiter.getName() + ";" + recruiter.getSurname() + ";" + recruiter.getUsername() + ";" +
                            recruiter.getEmailAddress() + ";" + recruiter.getPassword() + ";" + recruiter.getRole() + ";" +
                            String.join(",", recruiter.getCompanies()); // Concatenate companies separated by a comma
                    lines.add(updatedLine); // Add the updated line
                } else {
                    lines.add(line); // Add the unchanged line
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
            return false;
        }

        // Rewrite the file with the updated data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Write a new line
            }
            return true;
        } catch (IOException e) {
            // Handle file writing error
            return false;
        }
    }

    @Override
    public boolean deleteRecruiter(String username) {
        if (!recruiterExists(username)) {
            return false; // The recruiter does not exist, so we cannot delete them.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && !data[2].trim().equals(username)) {
                    // Add only the lines that do not correspond to the recruiter to delete
                    lines.add(line);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
            return false;
        }

        // Rewrite the file without the deleted recruiter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Write a new line
            }
            return true;
        } catch (IOException e) {
            // Handle file writing error
            return false;
        }
    }

    @Override
    public boolean recruiterExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7 && data[2].trim().equals(username)) {  // username is at the third position
                    return true;  // Recruiter found
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle both IOException and IllegalArgumentException
        }
        return false;  // Recruiter not found
    }
}
