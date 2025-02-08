    package org.example.togetjob.model.dao.concreteobjects;

    import org.example.togetjob.connection.DatabaseConfig;
    import org.example.togetjob.exceptions.DatabaseException;
    import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
    import org.example.togetjob.model.dao.abstractobjects.StudentDao;
    import org.example.togetjob.model.dao.abstractobjects.UserDao;
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

        private static final String UPDATE_STUDENT_SQL =
                "UPDATE STUDENT SET DateOfBirth = ?, PhoneNumber = ?, Degrees = ?, CourseAttended = ?, Certifications = ?, "
                        + "WorkExperience = ?, Skills = ?, Availability = ? WHERE Username = ?";

        private static final String DELETE_STUDENT_SQL =
                "DELETE FROM STUDENT WHERE Username = ?";

        private static final String CHECK_STUDENT_EXISTS_SQL =
                "SELECT COUNT(*) FROM STUDENT WHERE Username = ?";

        private static final String COLUMN_DATE_OF_BIRTH = "DateOfBirth";
        private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
        private static final String COLUMN_DEGREES = "Degrees";
        private static final String COLUMN_COURSE_ATTENDED = "CourseAttended";
        private static final String COLUMN_CERTIFICATIONS = "Certifications";
        private static final String COLUMN_WORK_EXPERIENCE = "WorkExperience";
        private static final String COLUMN_SKILLS = "Skills";
        private static final String COLUMN_AVAILABILITY = "Availability";

        private final UserDao userDao;
        private JobApplicationDao jobApplicationDao;

        public DataBaseStudentDao(UserDao userDao, JobApplicationDao jobApplicationDao) {
            this.userDao = userDao;
            this.jobApplicationDao = jobApplicationDao;
        }

        public void setJobApplicationDao(DataBaseJobApplicationDao jobApplicationDao) {
            this.jobApplicationDao = jobApplicationDao;
        }


        @Override
        public void saveStudent(Student student) {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT_SQL)) {

                stmt.setString(1, student.getUsername());
                stmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
                stmt.setString(3, student.getPhoneNumber());
                stmt.setString(4, String.join(",", student.getDegrees()));
                stmt.setString(5, String.join(",", student.getCourseAttended()));
                stmt.setString(6, String.join(",", student.getCertifications()));
                stmt.setString(7, String.join(",", student.getWorkExperiences()));
                stmt.setString(8, String.join(",", student.getSkills()));
                stmt.setString(9, student.getAvailability());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseException("Error save student");
            }
        }

        @Override
        public Optional<Student> getStudent(String username) {
            Optional<User> userOptional = userDao.getUser(username);

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

                    Student student = new Student(user.getName(), user.getSurname(), user.getUsername(), user.getEmailAddress(), user.getPassword(), user.getRole(),
                            dateOfBirth, phoneNumber, degrees, courseAttended, certifications,
                            workExperiences, skills, availability, new ArrayList<>()); //NULL

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
        public List<Student> getAllStudents() {
            List<Student> students = new ArrayList<>();
            List<User> users = userDao.getAllUsers();
            Map<String, Student> studentMap = getStudentsDetails(users);

            for (Student student : studentMap.values()) {
                List<JobApplication> jobApplications = jobApplicationDao.getAllJobApplications(student);
                student.setJobApplications(jobApplications);
                students.add(student);
            }

            return students;
        }

        private Map<String, Student> getStudentsDetails(List<User> users) {
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

                    Student student = new Student(user.getName(), user.getSurname(), user.getUsername(),
                            user.getEmailAddress(), user.getPassword(), user.getRole(), dateOfBirth, phoneNumber,
                            degrees, courseAttended, certifications, workExperiences, skills, availability, null); // JobApplications is null

                    studentMap.put(username, student);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error obtaining student details", e);
            }

            return studentMap;
        }

        private User findUserByUsername(List<User> users, String username) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
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

        @Override
        public boolean updateStudent(Student student) {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(UPDATE_STUDENT_SQL)) {

                stmt.setDate(1, Date.valueOf(student.getDateOfBirth()));
                stmt.setString(2, student.getPhoneNumber());
                stmt.setString(3, String.join(",", student.getDegrees()));
                stmt.setString(4, String.join(",", student.getCourseAttended()));
                stmt.setString(5, String.join(",", student.getCertifications()));
                stmt.setString(6, String.join(",", student.getWorkExperiences()));
                stmt.setString(7, String.join(",", student.getSkills()));
                stmt.setString(8, student.getAvailability());
                stmt.setString(9, student.getUsername());

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                throw new DatabaseException("Error updating student");
            }
        }

        @Override
        public boolean deleteStudent(String username) {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT_SQL)) {

                stmt.setString(1, username);
                int rowsDeleted = stmt.executeUpdate();
                return rowsDeleted > 0;
            } catch (SQLException e) {
                throw new DatabaseException("Error deleting student");
            }
        }

        @Override
        public boolean studentExists(String username) {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(CHECK_STUDENT_EXISTS_SQL)) {

                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            } catch (SQLException e) {
                throw new DatabaseException("Error Student not found");
            }
        }

    }
