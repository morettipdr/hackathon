package org.caixa.exception;

import io.quarkus.logging.Log;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if(exception instanceof BadRequestException){
            Log.error("An error occurred", exception);
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Unauthorized access: " + exception.getMessage())
                           .build();
        } else {
            Log.error("An error occurred", exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An unexpected error occurred: " + exception.getMessage())
                           .build();
        }
    }
}
