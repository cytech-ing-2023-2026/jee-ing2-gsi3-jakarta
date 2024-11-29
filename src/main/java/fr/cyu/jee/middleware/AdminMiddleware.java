package fr.cyu.jee.middleware;

import fr.cyu.jee.model.User;
import fr.cyu.jee.model.UserType;
import fr.cyu.jee.servlet.CrudResponse;
import fr.cyu.jee.servlet.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class AdminMiddleware extends LoginMiddleware {

    @Override
    public Optional<CrudResponse> handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return super.handle(req, resp)
                .or(() ->
                        ((User) req.getSession().getAttribute("user")).getUserType() == UserType.ADMIN
                                ? Optional.empty()
                                : Optional.of(ErrorResponse.FORBIDDEN)
                );
    }
}
