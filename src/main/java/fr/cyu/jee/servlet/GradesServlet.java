package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.AddGradeDTO;
import fr.cyu.jee.dto.DeleteGradeDTO;
import fr.cyu.jee.dto.EmptyDTO;
import fr.cyu.jee.dto.UpdateGradeDTO;
import fr.cyu.jee.model.*;
import fr.cyu.jee.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

@WebServlet("/grades")
public class GradesServlet extends CrudServlet<AddGradeDTO, EmptyDTO, UpdateGradeDTO, DeleteGradeDTO> {

    public GradesServlet() {
        super(AddGradeDTO.class, EmptyDTO.class, UpdateGradeDTO.class, DeleteGradeDTO.class);
    }

    private UserRepository userRepository = ServiceManager.getInstance().getService(ServiceKey.USER_REPOSITORY);

    private GradeRepository gradeRepository = ServiceManager.getInstance().getService(ServiceKey.GRADE_REPOSITORY);

    private SubjectRepository subjectRepository = ServiceManager.getInstance().getService(ServiceKey.SUBJECT_REPOSITORY);

    @Override
    public CrudResponse onPost(AddGradeDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");

        Optional<Student> studentOpt = userRepository.findByEmailAndType(dto.getEmail(), UserType.STUDENT);
        if (studentOpt.isEmpty()) return new ModelAndView("redirect:/grades", Map.of("error", "Email not found: " + dto.getEmail()));
        Student student = studentOpt.get();

        return switch (user) {

            case Admin ignored -> {
                if(dto.getSubjectOptional().isPresent()) {
                    student.addGrade(dto.getSubject(), dto.getGrade());
                    userRepository.save(student);
                    yield new ModelAndView("redirect:/grades", Map.of("message", student.getFirstName() + " " + student.getLastName() + " got the grade " + dto.getGrade() + " in " + dto.getSubject().getName()));
                } else {
                    yield new ModelAndView("redirect:/grades", Map.of("error", "You must define a subject for this grade"));
                }
            }

            case Teacher teacher -> {
                if(dto.getSubjectOptional().isPresent()) {
                    yield new ModelAndView("redirect:/grades", Map.of("error", "You cannot specify a subject as teacher"));
                } else {
                    student.addGrade(teacher.getSubject(), dto.getGrade());
                    userRepository.save(student);
                    yield new ModelAndView("redirect:/grades", Map.of("message", student.getFirstName() + " " + student.getLastName() + " got the grade " + dto.getGrade() + " in " + teacher.getSubject().getName()));
                }
            }
            default -> ErrorResponse.FORBIDDEN;
        };
    }

    @Override
    public CrudResponse onGet(EmptyDTO emptyDTO, HttpSession session) {
        return switch ((User) session.getAttribute("user")) {
            case Admin ignored -> new ModelAndView("teacher_grades", Map.ofEntries(
                    Map.entry("grades", gradeRepository.getAllOrdered()),
                    Map.entry("subjects", subjectRepository.findAll())
            ));
            case Teacher teacher -> new ModelAndView("teacher_grades", Map.ofEntries(
                    Map.entry("grades", gradeRepository.getAllBySubjectOrdered(teacher.getSubject().getId()))
            ));
            case Student student -> new ModelAndView("student_grades", Map.ofEntries(
                    Map.entry("grades", gradeRepository.getAllByStudentOrdered(student.getId()))
            ));
            default -> throw new AssertionError("Invalid user type ");
        };
    }

    @Override
    public CrudResponse onPut(UpdateGradeDTO dto, HttpSession session) {
        return switch ((User) session.getAttribute("user")) {
            case Admin ignored -> {
                Grade grade = dto.getGrade();
                grade.setValue(dto.getValue());
                dto.getSubjectOptional().ifPresent(grade::setSubject);
                gradeRepository.save(grade);
                yield new ModelAndView("redirect:/grades");
            }

            case Teacher teacher -> {
                if(teacher.getSubject().getId() != dto.getGrade().getSubject().getId())
                    yield new ModelAndView("redirect:/grades", Map.of("error", "This grade is not associated with your teaching subject"));
                else if(dto.getSubjectOptional().isPresent())
                    yield new ModelAndView("redirect:/grades", Map.of("error", "Only admins can change the subject of a grade"));
                else {
                    Grade grade = dto.getGrade();
                    grade.setValue(dto.getValue());
                    gradeRepository.save(grade);
                    yield new ModelAndView("redirect:/grades");
                }
            }

            default -> ErrorResponse.FORBIDDEN;
        };
    }

    @Override
    public CrudResponse onDelete(DeleteGradeDTO dto, HttpSession session) {
        return switch ((User) session.getAttribute("user")) {
            case Admin ignored -> {
                Student student = dto.getGrade().getStudent();
                student.getGrades().remove(dto.getGrade());
                gradeRepository.delete(dto.getGrade());
                userRepository.save(student);
                yield new ModelAndView("redirect:/grades");
            }
            case Teacher teacher -> {
                if(teacher.getSubject().getId() == dto.getGrade().getSubject().getId()) {
                    Student student = dto.getGrade().getStudent();
                    student.getGrades().remove(dto.getGrade());
                    gradeRepository.delete(dto.getGrade());
                    userRepository.save(student);
                    yield new ModelAndView("redirect:/grades");
                } else {
                    yield new ModelAndView("redirect:/grades", Map.of("error", "This grade is not associated with your teaching subject"));
                }
            }

            default -> ErrorResponse.FORBIDDEN;
        };
    }
}
