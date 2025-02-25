    package org.example.togetjob.dao.concreteobjects;

    import org.example.togetjob.connection.DatabaseConfig;
    import org.example.togetjob.exceptions.DatabaseException;
    import org.example.togetjob.dao.abstractobjects.StudentDao;
    import org.example.togetjob.model.entity.JobApplication;
    import org.example.togetjob.model.entity.Student;
    import org.example.togetjob.model.entity.User;

    import java.sql.*;
    import java.sql.Date;
    import java.time.LocalDate;
    import java.util.*;

    public class DataBaseStudentDao implements StudentDao {

        private static final String INSERT_STUDENT_SQL =
                "INSERT INTO STUDENT (Username, DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperience, Skills, Availability) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        private static final String SELECT_STUDENT_SQL =
                "SELECT DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperience, Skills, Availability "
                        + "FROM STUDENT WHERE Username = ?";

        private static final String SELECT_ALL_STUDENTS_SQL =
                "SELECT Username, DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperience, Skills, Availability "
                        + "FROM STUDENT";

        private static final String COLUMN_DATE_OF_BIRTH = "DateOfBirth";
        private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
        private static final String COLUMN_DEGREES = "Degrees";
        private static final String COLUMN_COURSE_ATTENDED = "CourseAttended";
        private static final String COLUMN_CERTIFICATIONS = "Certifications";
        private static final String COLUMN_WORK_EXPERIENCE = "WorkExperience";
        private static final String COLUMN_SKILLS = "Skills";
        private static final String COLUMN_AVAILABILITY = "Availability";

        private final DataBaseUserDao dataBaseUserDao;
        private DataBaseJobApplicationDao jobApplicationDao;

        public DataBaseStudentDao(DataBaseUserDao dataBaseUserDao, DataBaseJobApplicationDao jobApplicationDao) {
            this.dataBaseUserDao = dataBaseUserDao;
            this.jobApplicationDao = jobApplicationDao;
        }

        public void setJobApplicationDao(DataBaseJobApplicationDao jobApplicationDao) {
            this.jobApplicationDao = jobApplicationDao;
        }


        @Override
        public void saveStudent(Student student) throws DatabaseException {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT_SQL)) {

                stmt.setString(1, student.obtainUsername());
                stmt.setDate(2, Date.valueOf(student.obtainDateOfBirth()));
                stmt.setString(3, student.obtainPhoneNumber());
                stmt.setString(4, String.join(",", student.obtainDegrees()));
                stmt.setString(5, String.join(",", student.obtainCoursesAttended()));
                stmt.setString(6, String.join(",", student.obtainCertifications()));
                stmt.setString(7, String.join(",", student.obtainWorkExperiences()));
                stmt.setString(8, String.join(",", student.obtainSkills()));
                stmt.setString(9, student.obtainAvailability());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseException("Error save student");
            }
        }

        @Override
        public Optional<Student> getStudent(String username) {
            Optional<User> userOptional = dataBaseUserDao.getUser(username);

            if (userOptional.isEmpty()) {
                return Optional.empty();
            }

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_SQL)) {

                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    User user = userOptional.get();

                    LocalDate dateOfBirth = rs.getDate(COLUMN_DATE_OF_BIRTH).toLocalDate();
                    String phoneNumber = rs.getString(COLUMN_PHONE_NUMBER);
                    String degreesStr = rs.getString(COLUMN_DEGREES);
                    List<String> degrees = degreesStr != null ? List.of(degreesStr.split(",")) : Collections.emptyList();

                    String courseAttendedStr = rs.getString(COLUMN_COURSE_ATTENDED);
                    List<String> courseAttended = courseAttendedStr != null ? List.of(courseAttendedStr.split(",")) : Collections.emptyList();

                    String certificationsStr = rs.getString(COLUMN_CERTIFICATIONS);
                    List<String> certifications = certificationsStr != null ? List.of(certificationsStr.split(",")) : Collections.emptyList();

                    String workExperienceStr = rs.getString(COLUMN_WORK_EXPERIENCE);
                    List<String> workExperiences = workExperienceStr != null ? List.of(workExperienceStr.split(",")) : Collections.emptyList();

                    String skillsStr = rs.getString(COLUMN_SKILLS);
                    List<String> skills = skillsStr != null ? List.of(skillsStr.split(",")) : Collections.emptyList();

                    String availability = rs.getString(COLUMN_AVAILABILITY);

                    Student student = new Student(
                            user.obtainName(),            // name
                            user.obtainSurname(),         // surname
                            user.obtainUsername(),        // username
                            user.obtainEmailAddress(),    // emailAddress
                            user.obtainPassword(),        // password
                            user.obtainRole(),            // role
                            dateOfBirth                // dateOfBirth
                    );

                    student.setPhoneNumber(phoneNumber);             // phoneNumber
                    student.setDegrees(degrees);                     // degrees
                    student.setCoursesAttended(courseAttended);      // courses attended
                    student.setCertifications(certifications);       // certifications
                    student.setWorkExperiences(workExperiences);     // work experiences
                    student.setSkills(skills);                       // skills
                    student.setAvailability(availability);           // availability
                    student.setJobApplications(new ArrayList<>());     // jobApplications

                    if (jobApplicationDao != null) {
                        List<JobApplication> jobApplications = jobApplicationDao.getAllJobApplications(student);
                        student.setJobApplications(jobApplications);
                    }

                    return Optional.of(student);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error obtaining student for username: " + username + ", SQL error: " + e.getMessage(), e);
            }
            return Optional.empty();
        }


        @Override
        public List<Student> getAllStudents() throws DatabaseException {
            try {
                List<Student> students = new ArrayList<>();
                List<User> users = dataBaseUserDao.getAllUsers();
                Map<String, Student> studentMap = getStudentsDetails(users);

                for (Student student : studentMap.values()) {
                    List<JobApplication> jobApplications = jobApplicationDao.getAllJobApplications(student);
                    student.setJobApplications(jobApplications);
                    students.add(student);
                }
                return students;
            }catch(DatabaseException e){
                throw new DatabaseException(e.getMessage()) ;
            }
        }

        private Map<String, Student> getStudentsDetails(List<User> users) throws DatabaseException{
            Map<String, Student> studentMap = new HashMap<>();

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_STUDENTS_SQL);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("Username");

                    User user = findUserByUsername(users, username);
                    if (user == null) {
                        continue;
                    }

                    LocalDate dateOfBirth = rs.getDate(COLUMN_DATE_OF_BIRTH) != null ? rs.getDate(COLUMN_DATE_OF_BIRTH).toLocalDate() : null;
                    String phoneNumber = rs.getString(COLUMN_PHONE_NUMBER);

                    List<String> degrees = convertCsvToList(rs.getString(COLUMN_DEGREES));
                    List<String> courseAttended = convertCsvToList(rs.getString(COLUMN_COURSE_ATTENDED));
                    List<String> certifications = convertCsvToList(rs.getString(COLUMN_CERTIFICATIONS));
                    List<String> workExperiences = convertCsvToList(rs.getString(COLUMN_WORK_EXPERIENCE));
                    List<String> skills = convertCsvToList(rs.getString(COLUMN_SKILLS));
                    String availability = rs.getString(COLUMN_AVAILABILITY);


                    Student student = new Student(
                            user.obtainName(),             // name
                            user.obtainSurname(),          // surname
                            user.obtainUsername(),         // username
                            user.obtainEmailAddress(),     // emailAddress
                            user.obtainPassword(),         // password
                            user.obtainRole(),             // role
                            dateOfBirth                 // dateOfBirth
                    );

                    student.setPhoneNumber(phoneNumber);             // phoneNumber
                    student.setDegrees(degrees);                     // degrees
                    student.setCoursesAttended(courseAttended);      // courses attended
                    student.setCertifications(certifications);       // certifications
                    student.setWorkExperiences(workExperiences);     // work experiences
                    student.setSkills(skills);                       // skills
                    student.setAvailability(availability);           // availability
                    student.setJobApplications(null);                 // jobApplications, null
                    studentMap.put(username, student);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error obtaining student details", e);
            }

            return studentMap;
        }

        private User findUserByUsername(List<User> users, String username) {
            for (User user : users) {
                if (user.obtainUsername().equals(username)) {
                    return user;
                }
            }
            return null;
        }

        private List<String> convertCsvToList(String csvData) {
            if (csvData == null || csvData.isEmpty()) {
                return new ArrayList<>();
            }
            return List.of(csvData.split(","));
        }

    }
