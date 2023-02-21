package ru.otus.library.authentication;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(final HttpServletRequest httpServletRequest,
                       final HttpServletResponse httpServletResponse,
                       final AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.getOutputStream().print("Access denied");
        httpServletResponse.setStatus(403);
    }
}
