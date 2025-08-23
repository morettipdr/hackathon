package org.caixa.exception;

import io.micrometer.core.instrument.search.MeterNotFoundException;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
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

        if(exception instanceof MeterNotFoundException){
            Log.error("An error occurred", exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("A aplicação não encontrou telemetria")
                    .build();
        }

        Log.error("An unexpected error occurred", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(exception.getMessage())
                       .build();
    }
}
