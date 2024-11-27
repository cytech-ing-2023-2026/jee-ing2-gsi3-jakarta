package fr.cyu.jee.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public record ModelAndView(View view, Map<String, Object> attributes) implements CrudResponse {

    public ModelAndView(String view, Map<String, Object> attributes) {
        this(View.parse("/jsp", view), attributes);
    }

    public ModelAndView(View view) {
        this(view, new HashMap<>());
    }

    public ModelAndView(String view) {
        this(View.parse("/jsp", view));
    }

    @Override
    public void apply(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (view) {
            case View.Forward(String path) -> {
                for(Map.Entry<String, Object> attribute : attributes.entrySet()) {
                    request.setAttribute(attribute.getKey(), attribute.getValue());
                }

                servletContext.getRequestDispatcher(path).forward(request, response);
            }

            case View.Redirect(String path) -> {
                request.getSession().setAttribute("redirect_attributes", attributes);
                response.sendRedirect(path);
            }
        }
    }
}
