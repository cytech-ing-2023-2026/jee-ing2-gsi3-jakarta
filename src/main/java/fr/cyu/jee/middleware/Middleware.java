package fr.cyu.jee.middleware;

import fr.cyu.jee.model.User;
import fr.cyu.jee.model.UserType;
import fr.cyu.jee.servlet.CrudResponse;
import fr.cyu.jee.servlet.ErrorResponse;
import fr.cyu.jee.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public interface Middleware {

    Optional<CrudResponse> handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    Middleware LOGIN = (req, resp) ->
            req.getSession().getAttribute("user") == null
                    ? Optional.of(new ModelAndView("redirect:/login?redirect=" + URI.create(req.getRequestURI()).toASCIIString()))
                    : Optional.empty();

    Middleware ADMIN = (req, resp) -> LOGIN.handle(req, resp).or(
            () ->
                    ((User) req.getSession().getAttribute("user")).getUserType() == UserType.ADMIN
                            ? Optional.empty()
                            : Optional.of(ErrorResponse.FORBIDDEN)
    );
}
