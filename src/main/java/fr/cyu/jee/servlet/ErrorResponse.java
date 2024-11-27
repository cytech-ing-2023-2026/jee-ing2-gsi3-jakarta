package fr.cyu.jee.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public record ErrorResponse(int code, String message) implements CrudResponse {

    @Override
    public void apply(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(code, message);
    }
}
