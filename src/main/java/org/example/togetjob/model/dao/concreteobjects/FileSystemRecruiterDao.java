package org.example.togetjob.model.dao.concreteobjects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemRecruiterDao implements RecruiterDao {

    private static final String PATH_NAME = "/Users/enrico_talone/IdeaProjects/toGetJob/src/main/resources/org/example/togetjob/files_json/Recruiter.json";
    //private static final String PATH_NAME = "files_json/Recruiter.json" ;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean saveRecruiter(Recruiter recruiter) {
        try {
            List<Recruiter> recruiters = getAllRecruiter();

            if (recruiters.stream().anyMatch(r -> r.getUsername().equals(recruiter.getUsername()))) {
                System.out.println("The recruiter: " + recruiter.getUsername() + " already exists.");
                return false;
            }

            recruiters.add(recruiter);
            objectMapper.writeValue(new File(PATH_NAME), recruiters);
            System.out.println("The recruiter: " + recruiter.getUsername() + " has been successfully saved in the File System");
            return true;

        } catch (IOException e) {
            System.out.println("The recruiter: " + recruiter.getUsername() + " cannot be saved in the File System");
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        return getAllRecruiter().stream().filter(recruiter -> recruiter.getUsername().equals(username)).findFirst();
    }


    @Override
    public List<Recruiter> getAllRecruiter() {
        try {
            List<Recruiter> recruiters = new ArrayList<>(objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<Recruiter>>() {}));

            if (recruiters.isEmpty()) {
                return new ArrayList<>();
            }

            return recruiters;

        } catch (IOException e) {
            System.out.println("Recruiters cannot be retrieved from File System");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        try {
            List<Recruiter> recruiters = getAllRecruiter();
            boolean found = false;

            for (int i = 0; i < recruiters.size(); i++) {
                if (recruiters.get(i).getUsername().equals(recruiter.getUsername())) {
                    recruiters.set(i, recruiter); // Update recruiter data
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("The recruiter: " + recruiter.getUsername() + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), recruiters);
            System.out.println("The recruiter: " + recruiter.getUsername() + " has been successfully updated.");
            return true;

        } catch (IOException e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRecruiter(String username) {
        try {
            List<Recruiter> recruiters = getAllRecruiter();
            boolean removed = recruiters.removeIf(recruiter -> recruiter.getUsername().equals(username));
            if (!removed) {
                System.out.println("The recruiter: " + username + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), recruiters);
            System.out.println("The recruiter: " + username + " has been successfully deleted.");
            return true;

        } catch (IOException e) {
            System.out.println("The recruiter: " + username + " cannot be deleted");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean recruiterExists(String username) {
        try {
            List<Recruiter> recruiters = objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<Recruiter>>() {});

            if (recruiters.isEmpty()) {
                System.out.println("There aren't any recruiters in the File System");
                return false;
            }

            boolean presence = recruiters.stream().anyMatch(recruiter -> recruiter.getUsername().equals(username));

            if (presence) {
                System.out.println("The recruiter: " + username + " exists");
                return true;
            }

            System.out.println("The recruiter: " + username + " doesn't exist");
            return false;

        } catch (IOException e) {
            System.out.println("The recruiter: " + username + " cannot be searched");
            System.out.println(e.getMessage());
            return false;
        }
    }

}
