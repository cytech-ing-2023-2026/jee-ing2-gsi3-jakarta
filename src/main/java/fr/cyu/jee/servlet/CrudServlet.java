package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.DTOResult;
import fr.cyu.jee.dto.DTOUtil;
import fr.cyu.jee.middleware.Middleware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class CrudServlet<PostDTO, GetDTO, PutDTO, DeleteDTO> extends HttpServlet {

    private Class<PostDTO> postDTOClass;
    private Class<GetDTO> getDTOClass;
    private Class<PutDTO> putDTOClass;
    private Class<DeleteDTO> deleteDTOClass;
    private Map<String, List<Middleware>> middlewares;

    public CrudServlet(Class<PostDTO> postDTOClass, Class<GetDTO> getDTOClass, Class<PutDTO> putDTOClass, Class<DeleteDTO> deleteDTOClass, Map<String, List<Middleware>> middlewares) {
        this.postDTOClass = postDTOClass;
        this.getDTOClass = getDTOClass;
        this.putDTOClass = putDTOClass;
        this.deleteDTOClass = deleteDTOClass;
        this.middlewares = middlewares;
    }

    public CrudServlet(Class<PostDTO> postDTOClass, Class<GetDTO> getDTOClass, Class<PutDTO> putDTOClass, Class<DeleteDTO> deleteDTOClass) {
        this(postDTOClass, getDTOClass, putDTOClass, deleteDTOClass, Map.of());
    }

    public CrudResponse onPost(PostDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    public CrudResponse onGet(GetDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    public CrudResponse onPut(PutDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    public CrudResponse onDelete(DeleteDTO dto, HttpSession session) {
        return CrudResponse.NOT_FOUND;
    }

    private <T> void processRequest(HttpServletRequest req, HttpServletResponse resp, Class<T> clazz, BiFunction<T, HttpSession, CrudResponse> logic) throws ServletException, IOException {
        Map<String, Object> redirectAttributes = (Map<String, Object>) req.getSession().getAttribute("redirect_attributes");

        if(redirectAttributes != null) {
            for(Map.Entry<String, Object> attribute : redirectAttributes.entrySet())
                req.setAttribute(attribute.getKey(), attribute.getValue());

            req.getSession().removeAttribute("redirect_attributes");
        }

        req.getParameterNames().asIterator().forEachRemaining(p -> req.setAttribute(p, req.getParameter(p)));

        Map<String, Object> map = new HashMap<>();
        req.getAttributeNames().asIterator().forEachRemaining(name -> map.put(name, req.getAttribute(name)));

        Optional<CrudResponse> middlewareResponse = Optional.empty();

        for(Middleware middleware : middlewares.getOrDefault(req.getMethod().toLowerCase(), List.of())) {
            middlewareResponse = middleware.handle(req, resp);
            if(middlewareResponse.isPresent()) break;
        }

        CrudResponse response = middlewareResponse.orElseGet(() -> switch (DTOUtil.decodeValid(map, clazz)) {
             case DTOResult.Success(T dto) -> logic.apply(dto, req.getSession());
             case DTOResult.DecodingFailure() -> CrudResponse.BAD_REQUEST_DECODING_FAILURE;
             case DTOResult.ValidationFailure(Set<ConstraintViolation<T>> violations) -> {
                 String errMsg = violations
                         .stream()
                         .map(err -> err.getPropertyPath().toString() + ": " + err.getMessage())
                         .reduce("", (a, b) -> a + "<br>" + b);

                 yield new ModelAndView(new View.Redirect(req.getHeader("referer")), Map.of("error", errMsg));
             }
         });

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
        String method = req.getParameter("method");
        if(method == null) processRequest(req, resp, postDTOClass, this::onPost);
        else switch (method) {
            case "put"    -> processRequest(req, resp, putDTOClass, this::onPut);
            case "delete" -> processRequest(req, resp, deleteDTOClass, this::onDelete);
            default -> processRequest(req, resp, postDTOClass, this::onPost);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp, getDTOClass, this::onGet);
    }
}
