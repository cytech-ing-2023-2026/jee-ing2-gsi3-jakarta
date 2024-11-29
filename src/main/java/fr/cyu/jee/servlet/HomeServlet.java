package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.EmptyDTO;
import fr.cyu.jee.middleware.Middleware;
import fr.cyu.jee.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@WebServlet("")
public class HomeServlet extends GetOnlyServlet<EmptyDTO> {

    public HomeServlet() {
        super(EmptyDTO.class, Map.of("get", List.of(Middleware.LOGIN)));
    }

    @Override
    public CrudResponse onGet(EmptyDTO emptyDTO, HttpSession session) {
        return switch (((User)session.getAttribute("user")).getUserType()) {
            case ADMIN -> new ModelAndView("admin_menu");
            case TEACHER, STUDENT -> new ModelAndView("user_menu");
        };
    }
}
