package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.DeleteUserDTO;
import fr.cyu.jee.dto.RegisterDTO;
import fr.cyu.jee.dto.UpdateUserDTO;
import fr.cyu.jee.dto.UserMenuDTO;
import fr.cyu.jee.middleware.Middleware;
import fr.cyu.jee.model.*;
import fr.cyu.jee.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/users")
public class UserServlet extends CrudServlet<RegisterDTO, UserMenuDTO, UpdateUserDTO, DeleteUserDTO> {

    public UserServlet() {
        super(RegisterDTO.class, UserMenuDTO.class, UpdateUserDTO.class, DeleteUserDTO.class, Map.ofEntries(
                Map.entry("get", List.of(Middleware.ADMIN)),
                Map.entry("post", List.of(Middleware.ADMIN)),
                Map.entry("put", List.of(Middleware.ADMIN)),
                Map.entry("delete", List.of(Middleware.ADMIN))
        ));
    }

    private SubjectRepository subjectRepository = ServiceManager.getInstance().getService(ServiceKey.SUBJECT_REPOSITORY);

    private UserRepository userRepository = ServiceManager.getInstance().getService(ServiceKey.USER_REPOSITORY);

    private CourseRepository courseRepository = ServiceManager.getInstance().getService(ServiceKey.COURSE_REPOSITORY);

    private AuthService authService = ServiceManager.getInstance().getService(ServiceKey.AUTH_SERVICE);

    @Override
    public CrudResponse onPost(RegisterDTO registerDTO, HttpSession session) {
        if(registerDTO.getUserType() == UserType.TEACHER && registerDTO.getSubjectOptional().isEmpty())
            return new ModelAndView("redirect:/admin/users?selectedMenu=ADD_MENU", Map.of("error", "A teacher must have an assigned subject"));
        Optional<User> registered = authService.register(registerDTO);
        if(registered.isPresent()) return new ModelAndView("redirect:/admin/users?selectedMenu=ADD_MENU", Map.of("message", "Successfully registered " + registered.get().getEmail()));
        else return new ModelAndView("redirect:/admin/users?selectedMenu=ADD_MENU", Map.of("error", "Email already taken"));
    }

    @Override
    public CrudResponse onGet(UserMenuDTO dto, HttpSession session) {
        if(dto.getSelectedMenu() == null) return new ModelAndView("admin_users_menu");
        else return switch (dto.getSelectedMenu()) {
            case ADD_MENU -> {
                Iterable<Subject> subjectsIt = subjectRepository.findAll();
                List<Subject> subjects = new ArrayList<>();
                subjectsIt.forEach(subjects::add);

                yield new ModelAndView("admin_add_users", Map.of("subjects", subjects));
            }
            case ALL_MENU -> new ModelAndView("admin_users_menu");
            case EDIT_MENU ->
                    new ModelAndView("admin_modify_users", Map.of("user", dto.getSelectedUser(), "users", userRepository.findAllByOrderByIdAsc()));
            case DISPLAY_MENU -> new ModelAndView("admin_display_users", Map.ofEntries(
                    Map.entry("users", userRepository
                            .findAllByOrderByIdAsc()
                            .stream()
                            .filter(dto::check)
                            .toList()
                    )
            ));
        };
    }

    @Override
    public CrudResponse onPut(UpdateUserDTO dto, HttpSession session) {
        User currentUser = dto.getUser();

        if(currentUser.getUserType() == UserType.TEACHER && dto.getSubject() == null)
            return new ModelAndView("redirect:/admin/users?selectedMenu=DISPLAY_MENU", Map.of("error", "Teacher must have a subject"));

        currentUser.setFirstName(dto.getFirstName());
        currentUser.setLastName(dto.getLastName());

        if(!dto.getEmail().equals(currentUser.getEmail()) && userRepository.findByEmail(dto.getEmail()).isPresent())
            return new ModelAndView("redirect:/admin/users?selectedMenu=DISPLAY_MENU", Map.of("error", "This email is already taken"));
        currentUser.setEmail(dto.getEmail());
        currentUser.setPassword(dto.getPassword());
        currentUser.setDob(dto.getDob());
        if(currentUser.getUserType() == UserType.TEACHER) ((Teacher) currentUser).setSubject(dto.getSubject());

        userRepository.save(currentUser);

        return new ModelAndView("redirect:/admin/users?selectedMenu=DISPLAY_MENU", Map.of("message", "Successfully updated user"));
    }

    @Override
    public CrudResponse onDelete(DeleteUserDTO dto, HttpSession session) {
        User currentUser = dto.getUser();
        if(currentUser.getId() == ((User) session.getAttribute("user")).getId()) return new ModelAndView("redirect:/admin/users?selectedMenu=DISPLAY_MENU", Map.of("error", "Cannot delete yourself"));

        if(currentUser instanceof Student student) {
            Set<Course> courses = student.getCourses();
            courses.forEach(course -> course.getStudents().remove(student));
            courseRepository.saveAll(courses);
        }

        userRepository.delete(currentUser);
        return new ModelAndView("redirect:/admin/users?selectedMenu=DISPLAY_MENU", Map.of("message", "Successfully deleted user"));
    }
}
