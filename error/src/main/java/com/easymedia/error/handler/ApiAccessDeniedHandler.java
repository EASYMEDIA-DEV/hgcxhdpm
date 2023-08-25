package com.easymedia.error.handler;

import com.easymedia.error.ErrorCode;
import com.easymedia.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(accessDeniedException instanceof InvalidCsrfTokenException || accessDeniedException instanceof MissingCsrfTokenException) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED_CSRF);
            try{
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}