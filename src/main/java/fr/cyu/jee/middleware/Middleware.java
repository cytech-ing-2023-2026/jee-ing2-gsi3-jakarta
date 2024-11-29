package fr.cyu.jee.middleware;

import fr.cyu.jee.servlet.CrudResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface Middleware {

    Optional<CrudResponse> handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
