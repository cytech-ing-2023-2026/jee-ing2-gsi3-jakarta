package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.*;
import fr.cyu.jee.middleware.Middleware;
import fr.cyu.jee.model.*;
import fr.cyu.jee.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/course")
public class CoursesServlet extends CrudServlet<AddCourseDTO, GetCourseDTO, UpdateCourseDTO, DeleteCourseDTO> {

    public CoursesServlet() {
        super(AddCourseDTO.class, GetCourseDTO.class, UpdateCourseDTO.class, DeleteCourseDTO.class, Map.ofEntries(
                Map.entry("get", List.of(Middleware.LOGIN)),
                Map.entry("post", List.of(Middleware.ADMIN)),
                Map.entry("put", List.of(Middleware.ADMIN)),
                Map.entry("delete", List.of(Middleware.ADMIN))
        ));
    }

    private CourseRepository courseRepository = ServiceManager.getInstance().getService(ServiceKey.COURSE_REPOSITORY);

    private UserRepository userRepository = ServiceManager.getInstance().getService(ServiceKey.USER_REPOSITORY);

    private SubjectRepository subjectRepository = ServiceManager.getInstance().getService(ServiceKey.SUBJECT_REPOSITORY);

    @Override
    public CrudResponse onPost(AddCourseDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user instanceof Admin) {
            String studentsRaw = dto.getStudents();

            Set<Student> students = studentsRaw == null
                    ? new HashSet<>()
                    : Arrays
                        .stream(studentsRaw.split(","))
                        .map(Integer::parseInt)
                        .flatMap(id -> userRepository.findById(id).stream())
                        .filter(usr -> usr.getUserType() == UserType.STUDENT)
                        .map(usr -> (Student) usr)
                        .collect(Collectors.toSet());

            Course course = new Course(
                    dto.getBeginDate(),
                    dto.getDuration(),
                    dto.getSubject(),
                    dto.getTeacher(),
                    students
            );
            courseRepository.save(course);
            return new ModelAndView("redirect:/course", Map.of("message", "Course created successfully."));
        } else {
            return ErrorResponse.FORBIDDEN;
        }
    }

    @Override
    public CrudResponse onGet(GetCourseDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LocalDate today = dto.getDateOptional().orElse(LocalDate.now());
        LocalDate monday = today.minusDays(today.getDayOfWeek().ordinal());
        LocalDate sunday = monday.plusDays(6);

        return switch (user) {
            case Admin ignored -> {
                List<Course> courses = new ArrayList<>();
                courseRepository.findAll().forEach(courses::add);

                List<Subject> subjects = new ArrayList<>();
                subjectRepository.findAll().forEach(subjects::add);

                yield new ModelAndView("admin_course", Map.ofEntries(
                        Map.entry("courses", courses),
                        Map.entry("subjects", subjects),
                        Map.entry("students", userRepository.findAllByTypeOrdered(UserType.STUDENT)),
                        Map.entry("teachers", userRepository.findAllByTypeOrdered(UserType.TEACHER))
                ));
            }
            case Teacher teacher -> {
                Set<Course> courses = courseRepository.getTeacherCourses(teacher.getId(), monday, sunday);
                yield new ModelAndView("user_course", Map.ofEntries(
                        Map.entry("courses", courses),
                        Map.entry("selectedMonday", monday),
                        Map.entry("selectedSunday", sunday)
                ));
            }
            case Student student -> {
                Set<Course> courses = courseRepository.getStudentCourses(student.getId(), monday, sunday);
                yield new ModelAndView("user_course", Map.ofEntries(
                        Map.entry("courses", courses),
                        Map.entry("selectedMonday", monday),
                        Map.entry("selectedSunday", sunday)
                ));
            }
            default -> ErrorResponse.FORBIDDEN;
        };
    }

    @Override
    public CrudResponse onPut(UpdateCourseDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user instanceof Admin) {
            String studentsRaw = dto.getStudents();

            Set<Student> students = studentsRaw == null
                    ? new HashSet<>()
                    : Arrays
                        .stream(studentsRaw.split(","))
                        .map(Integer::parseInt)
                        .flatMap(id -> userRepository.findById(id).stream())
                        .filter(usr -> usr.getUserType() == UserType.STUDENT)
                        .map(usr -> (Student) usr)
                        .collect(Collectors.toSet());

            Course course = dto.getCourse();
            course.setBeginDate(dto.getBeginDate());
            course.setDuration(dto.getDuration());
            course.setSubject(dto.getSubject());
            course.setTeacher(dto.getTeacher());
            course.setStudents(students);
            courseRepository.save(course);
            return new ModelAndView("redirect:/course", Map.of("message", "Course updated successfully."));
        } else {
            return ErrorResponse.FORBIDDEN;
        }
    }

    @Override
    public CrudResponse onDelete(DeleteCourseDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user instanceof Admin) {
            courseRepository.delete(dto.getCourse());
            return new ModelAndView("redirect:/course", Map.of("message", "Course deleted successfully."));
        } else {
            return ErrorResponse.FORBIDDEN;
        }
    }
}
