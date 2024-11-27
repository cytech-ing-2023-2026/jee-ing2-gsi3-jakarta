package fr.cyu.jee.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public sealed interface CrudResponse permits ErrorResponse, ModelAndView {

    void apply(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    CrudResponse FORBIDDEN = new ErrorResponse(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    CrudResponse NOT_FOUND = new ErrorResponse(HttpServletResponse.SC_NOT_FOUND, "Not found");
    CrudResponse BAD_REQUEST_DECODING_FAILURE = new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Cannot decode the given form");
}
