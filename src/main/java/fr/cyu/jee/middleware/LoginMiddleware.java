package fr.cyu.jee.middleware;

import fr.cyu.jee.servlet.CrudResponse;
import fr.cyu.jee.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class LoginMiddleware implements Middleware {

    @Override
    public Optional<CrudResponse> handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return req.getSession().getAttribute("user") == null
                ? Optional.of(new ModelAndView(URI.create("/login?redirect=" + req.getRequestURI()).toASCIIString()))
                : Optional.empty();
    }
}
