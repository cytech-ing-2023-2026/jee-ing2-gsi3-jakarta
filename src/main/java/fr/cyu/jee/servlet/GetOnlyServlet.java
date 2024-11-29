package fr.cyu.jee.servlet;

import fr.cyu.jee.dto.EmptyDTO;
import fr.cyu.jee.middleware.Middleware;

import java.util.List;
import java.util.Map;

public class GetOnlyServlet<GetDTO> extends CrudServlet<EmptyDTO, GetDTO, EmptyDTO, EmptyDTO> {
    public GetOnlyServlet(Class<GetDTO> getDTOClass, Map<String, List<Middleware>> middlewares) {
        super(EmptyDTO.class, getDTOClass, EmptyDTO.class, EmptyDTO.class, middlewares);
    }

    public GetOnlyServlet(Class<GetDTO> getDTOClass) {
        super(EmptyDTO.class, getDTOClass, EmptyDTO.class, EmptyDTO.class);
    }
}
