package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;

import java.io.*;
import java.util.*;

public class FileSystemRecruiterDao implements RecruiterDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/Recruiter.txt";
    private static final String DELIMITER = ";";
    private static final String COMPANY_SEPARATOR = ",";

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        if (recruiterExists(recruiter.obtainUsername())) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(formatRecruiterData(recruiter));
            writer.newLine();
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        return getRecruitersFromFile().stream()
                .filter(recruiter -> recruiter.obtainUsername().equals(username))
                .findFirst();
    }

    private boolean recruiterExists(String username) {
        return getRecruitersFromFile().stream()
                .anyMatch(recruiter -> recruiter.obtainUsername().equals(username));
    }

    private List<Recruiter> getRecruitersFromFile() {
        List<Recruiter> recruiters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseRecruiterData(line).ifPresent(recruiters::add);
            }
        } catch (IOException e) {
            // Handle exception
        }
        return recruiters;
    }

    private Optional<Recruiter> parseRecruiterData(String line) {
        String[] data = line.split(DELIMITER);
        if (data.length >= 7) {
            try {
                Role role = Role.valueOf(data[5].trim());
                if (role == Role.RECRUITER) {
                    List<String> companies = Arrays.asList(data[6].split(COMPANY_SEPARATOR));
                    return Optional.of(new Recruiter(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            data[3].trim(), data[4].trim(), role, companies));
                }
            } catch (IllegalArgumentException e) {
                // Handle invalid role exception
            }
        }
        return Optional.empty();
    }

    private String formatRecruiterData(Recruiter recruiter) {
        return String.join(DELIMITER,
                recruiter.obtainName(),
                recruiter.obtainSurname(),
                recruiter.obtainUsername(),
                recruiter.obtainEmailAddress(),
                recruiter.obtainPassword(),
                recruiter.obtainRole().toString(),
                String.join(COMPANY_SEPARATOR, recruiter.obtainCompanies()));
    }

}
