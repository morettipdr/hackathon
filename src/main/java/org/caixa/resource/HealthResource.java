package org.caixa.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.caixa.model.dto.health.TelemetryDTO;
import org.caixa.model.dto.health.VolumeRequestDTO;
import org.caixa.service.HealthService;

@Path("/health")
public class HealthResource {

    @Inject
    HealthService healthService;

    @GET
    @Path("/volume")
    public Response getVolume(@BeanParam @Valid VolumeRequestDTO volumeRequestDTO) {
        return Response.ok().entity(healthService.getVolume(volumeRequestDTO)).build();
    }

    @GET
    @Path("/telemetria")
    public TelemetryDTO getTelemetry() {
        TelemetryDTO telemetry = new TelemetryDTO();
        return telemetry;
    }

}
