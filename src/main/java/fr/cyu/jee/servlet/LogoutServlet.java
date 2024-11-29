package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.EmptyDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends GetOnlyServlet<EmptyDTO> {

    public LogoutServlet() {
        super(EmptyDTO.class);
    }

    @Override
    public CrudResponse onGet(EmptyDTO emptyDTO, HttpSession session) {
        session.removeAttribute("user");
        return new ModelAndView("redirect:/login");
    }
}
