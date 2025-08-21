package org.caixa.exception;

import io.quarkus.logging.Log;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Log.info("Mapeando a exceção: " + exception.getClass().getName());

        if(exception instanceof BadRequestException){
            Log.error("An error occurred", exception);
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(exception.getMessage())
                           .build();
        }

        if(exception instanceof IllegalArgumentException){
            Log.error("An error occurred", exception);
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(exception.getMessage())
                           .build();
        }

        Log.error("An unexpected error occurred", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(exception.getMessage())
                       .build();
    }
}
