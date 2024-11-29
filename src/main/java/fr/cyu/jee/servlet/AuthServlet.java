package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.EmptyDTO;
import fr.cyu.jee.dto.LoginDTO;
import fr.cyu.jee.model.User;
import fr.cyu.jee.service.AuthService;
import fr.cyu.jee.service.ServiceKey;
import fr.cyu.jee.service.ServiceManager;
import fr.cyu.jee.service.UserRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class AuthServlet extends CrudServlet<LoginDTO, EmptyDTO, EmptyDTO, EmptyDTO> {

    private AuthService authService = ServiceManager.getInstance().getService(ServiceKey.AUTH_SERVICE);

    public AuthServlet() {
        super(LoginDTO.class, EmptyDTO.class, EmptyDTO.class, EmptyDTO.class);
    }

    @Override
    public CrudResponse onPost(LoginDTO loginDTO, HttpSession session) {
        Optional<User> loggedIn = authService.authenticate(loginDTO);
        if(loggedIn.isPresent()){
            session.setAttribute("user", loggedIn.get());
            return new ModelAndView("redirect:" + loginDTO.getRedirectOrHome());
        } else {
            return new ModelAndView("redirect:/login", Map.of("error", "Invalid email or password"));
        }
    }

    @Override
    public CrudResponse onGet(EmptyDTO emptyDTO, HttpSession session) {
        return new ModelAndView("login");
    }

}
