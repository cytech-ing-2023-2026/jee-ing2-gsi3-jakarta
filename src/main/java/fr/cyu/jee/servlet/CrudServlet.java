package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.DTOResult;
import fr.cyu.jee.dto.DTOUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class CrudServlet<PostDTO, GetDTO> extends HttpServlet {

    private Class<PostDTO> postDTOClass;
    private Class<GetDTO> getDTOClass;

    public CrudServlet(Class<PostDTO> postDTOClass, Class<GetDTO> getDTOClass) {
        this.postDTOClass = postDTOClass;
        this.getDTOClass = getDTOClass;
    }

    public CrudResponse onPost(PostDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    public CrudResponse onGet(GetDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    private <T> void processRequest(HttpServletRequest req, HttpServletResponse resp, Class<T> clazz, BiFunction<T, HttpSession, CrudResponse> logic) throws ServletException, IOException {
        Map<String, Object> redirectAttributes = (Map<String, Object>) req.getSession().getAttribute("redirect_attributes");
        if(redirectAttributes != null) {
            for(Map.Entry<String, Object> attribute : redirectAttributes.entrySet())
                req.setAttribute(attribute.getKey(), attribute.getValue());

            req.removeAttribute("redirect_attributes");
        }

        req.getParameterNames().asIterator().forEachRemaining(p -> req.setAttribute(p, req.getParameter(p)));

        CrudResponse response = switch (DTOUtil.decodeValid(req, clazz)) {
             case DTOResult.Success(T dto) -> logic.apply(dto, req.getSession());
             case DTOResult.DecodingFailure() -> CrudResponse.BAD_REQUEST_DECODING_FAILURE;
             case DTOResult.ValidationFailure(Set<ConstraintViolation<T>> violations) -> {
                 String errMsg = violations
                         .stream()
                         .map(err -> err.getPropertyPath().toString() + ": " + err.getMessage())
                         .reduce("", (a, b) -> a + "<br>" + b);

                 yield new ModelAndView(new View.Redirect(req.getHeader("referer")), Map.of("error", errMsg));
             }
         };

         switch (response) {
             case ModelAndView(View view, Map<String, Object> attributes) -> {
                 switch (view) {
                     case View.Forward(String path) -> {
                         for(Map.Entry<String, Object> attribute : attributes.entrySet())
                             req.setAttribute(attribute.getKey(), attribute.getValue());

                         getServletContext().getRequestDispatcher(path).forward(req, resp);
                     }

                     case View.Redirect(String redirect) -> {
                         req.getSession().setAttribute("redirect_attributes", attributes);
                         resp.sendRedirect(redirect);
                     }
                 }
             }

             case ErrorResponse(int code, String message) -> resp.sendError(code, message);
         }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp, postDTOClass, this::onPost);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp, getDTOClass, this::onGet);
    }
}
